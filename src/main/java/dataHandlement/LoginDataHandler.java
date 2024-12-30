package dataHandlement;

import com.github.windpapi4j.InitializationFailedException;
import com.github.windpapi4j.WinAPICallFailedException;
import com.github.windpapi4j.WinDPAPI;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LoginDataHandler {
    /**
     * File Path to credential File
     */
    private final String fileLocation;
    /**
     * Global Instance of WinDPAPI encryption Object
     */
    private final boolean useWinDPAPI;
    private WinDPAPI winDPAPI;
    private SecretKey aesKey;
    private static final String AES_ALGORITHM = "AES";  // Declare the constant


    public LoginDataHandler(Config config) {
        fileLocation = config.getUntisCredentialsFile();
        useWinDPAPI = WinDPAPI.isPlatformSupported();
        if (!useWinDPAPI) {
            System.out.println("The Windows Data Protection API (DPAPI) is not available on " + System.getProperty("os.name") + ". Falling back to AES");
            String secretKey = config.getAESKEY();
            aesKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey), AES_ALGORITHM);
        }
    }

    /**
     * Method to encryptWinDPAPI all elements in the given Array and returning the encrypted Strings after encryption with WinDPAPI encryption Service
     * @param pCredentials Array with credentials to encryptWinDPAPI
     * @return Array with encrypted credentials
     * @throws WinAPICallFailedException If Encryption Failed
     * @throws InitializationFailedException If Encryption Instance Initialization Failed
     */
    private String[] encryptWinDPAPI(String[] pCredentials) throws WinAPICallFailedException, InitializationFailedException {
        //Create Encryption Instance and output Array
        winDPAPI = WinDPAPI.newInstance(WinDPAPI.CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);
        String[] encryptedCredentials = new String[pCredentials.length];
        //For every String in given Array: encryptWinDPAPI using winDPAPI
        for (int x = 0; x < pCredentials.length; x++) {
            byte[] encryptedBytes = winDPAPI.protectData(pCredentials[x].getBytes(UTF_8));
            encryptedCredentials[x] = Base64.getEncoder().encodeToString(encryptedBytes);
        }
        return encryptedCredentials;
    }

    /**
     * Method to decryptWinDPAPI all elements in the given Array and returning the decrypted Strings after decryption with WinDPAPI decryption Service
     * @param pCredentials Array with credentials to decryptWinDPAPI
     * @return Array with decrypted credentials
     * @throws WinAPICallFailedException If Decryption Failed
     * @throws InitializationFailedException If Decryption Instance Initialization Failed
     */
    private String[] decryptWinDPAPI(String[] pCredentials) throws WinAPICallFailedException, InitializationFailedException {
        //Create Decryption Instance and output Array
        winDPAPI = WinDPAPI.newInstance(WinDPAPI.CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);
        String[] decryptedCredentials = new String[pCredentials.length];
        //For every String in given Array: decryptWinDPAPI using winDPAPI
        for (int x = 0; x < pCredentials.length; x++) {
            byte[] decryptedBytes = Base64.getMimeDecoder().decode(pCredentials[x]);
            decryptedCredentials[x] = new String(winDPAPI.unprotectData(decryptedBytes), UTF_8);
        }
        return decryptedCredentials;
    }

    private String[] encryptAES(String[] pCredentials) {
        String[] encryptedCredentials = new String[pCredentials.length];
        try {
            // Generate an Initialization Vector (IV) if you don't have one
            byte[] iv = new byte[16]; // 16 bytes for AES block size (128-bit)
            new SecureRandom().nextBytes(iv); // Fill with random data

            // Initialize Cipher with AES in CBC mode and PKCS5 padding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new IvParameterSpec(iv));

            for (int i = 0; i < pCredentials.length; i++) {
                byte[] encryptedBytes = cipher.doFinal(pCredentials[i].getBytes(UTF_8));
                // Combine the IV with the encrypted data and encode to Base64
                byte[] ivAndEncryptedData = new byte[iv.length + encryptedBytes.length];
                System.arraycopy(iv, 0, ivAndEncryptedData, 0, iv.length);
                System.arraycopy(encryptedBytes, 0, ivAndEncryptedData, iv.length, encryptedBytes.length);
                encryptedCredentials[i] = Base64.getEncoder().encodeToString(ivAndEncryptedData);
            }
            return encryptedCredentials;
        } catch (Exception e) {
            throw new RuntimeException("AES-encryption failed", e);
        }
    }


    private String[] decryptAES(String[] pCredentials) {
        String[] decryptedCredentials = new String[pCredentials.length];
        try {
            for (int i = 0; i < pCredentials.length; i++) {
                byte[] ivAndEncryptedData = Base64.getDecoder().decode(pCredentials[i]);

                // Extract IV and encrypted data
                byte[] iv = Arrays.copyOfRange(ivAndEncryptedData, 0, 16); // First 16 bytes are the IV
                byte[] encryptedData = Arrays.copyOfRange(ivAndEncryptedData, 16, ivAndEncryptedData.length); // Rest is encrypted data

                // Initialize Cipher with AES in CBC mode and PKCS5 padding
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));

                byte[] decryptedBytes = cipher.doFinal(encryptedData);
                decryptedCredentials[i] = new String(decryptedBytes, UTF_8);
            }
            return decryptedCredentials;
        } catch (Exception e) {
            throw new RuntimeException("AES-decryption failed", e);
        }
    }


    /**
     * Method to save and encryptWinDPAPI given credentials to json File
     * @param pCredentials Array with credentials to safely store
     * @throws WinAPICallFailedException Error while encrypting credentials
     */
    public void saveCredentials(String[] pCredentials) throws WinAPICallFailedException {
        //Create Arrays and JSON Objects
        JSONObject jsonObject = new JSONObject();
        String[] categories = new String[]{"username", "password", "school", "server"};
        String[] encryptedCredentials;
        //Encrypt credentials using winDPAPI encryptWinDPAPI Method
        try {
            if (useWinDPAPI) {
                encryptedCredentials = encryptWinDPAPI(pCredentials);
            } else {
                encryptedCredentials = encryptAES(pCredentials);
            }
        } catch (InitializationFailedException e) {
            throw new RuntimeException(e);
        }
        //Add credentials to Json Object
        for (int x = 0; x < Objects.requireNonNull(encryptedCredentials).length; x++) {
            jsonObject.put(categories[x], encryptedCredentials[x]);
        }
        //Write credentials to Json File
        try {
            FileWriter file = new FileWriter(fileLocation);
            file.write(jsonObject.toString());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store credentials");
        }
    }

    /**
     * Method to get decrypted credentials from saved credentials file
     * @return Array with decrypted credentials
     */
    public String[] getCredentials() {
        String[] credentials = new String[4];

        //Get credentials from environment variables
        credentials[0] = System.getenv("UNTISGOOGLESYNC_ULOGIN_" + "USERNAME");
        credentials[1] = System.getenv("UNTISGOOGLESYNC_ULOGIN_" + "PASSWORD");
        credentials[2] = System.getenv("UNTISGOOGLESYNC_ULOGIN_" + "SCHOOL");
        credentials[3] = System.getenv("UNTISGOOGLESYNC_ULOGIN_" + "SERVER");

        //Check if all encrypted credentials could be obtained
        boolean isEmptyVar = false;
        for (String credential : credentials) {
            if (credential == null || credential.isEmpty()) {
                isEmptyVar = true;
                break;
            }
        }

        //Check if environment variables did not provide all values
        if (isEmptyVar) {
            //Create Json Objects and Parser
            JsonObject jsonObject;
            //Read Json File
            try {
                jsonObject = JsonParser.parseReader(new FileReader(fileLocation)).getAsJsonObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Extract credentials from JSON file for missing environment values
            for (int i = 0; i < credentials.length; i++) {
                if (credentials[i] == null || credentials[i].isEmpty()) {
                    if (i == 0) credentials[0] = String.valueOf(jsonObject.get("username"));
                    if (i == 1) credentials[1] = String.valueOf(jsonObject.get("password"));
                    if (i == 2) credentials[2] = String.valueOf(jsonObject.get("school"));
                    if (i == 3) credentials[3] = String.valueOf(jsonObject.get("server"));
                }
            }
        }
        //Decrypt credentials using winDPAPI or AES decryption Methods
        try {
            if (useWinDPAPI) {
                credentials = decryptWinDPAPI(credentials);
            } else {
                credentials = decryptAES(credentials);
            }
        } catch (WinAPICallFailedException | InitializationFailedException e) {
            throw new RuntimeException(e);
        }
        return credentials;
    }
}
