package dataHandlement;

import com.github.windpapi4j.InitializationFailedException;
import com.github.windpapi4j.WinAPICallFailedException;
import com.github.windpapi4j.WinDPAPI;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private WinDPAPI winDPAPI;

    public LoginDataHandler(Config config) {
        fileLocation = config.getUntisCredentialsFile();
    }

    /**
     * Method to encrypt all elements in the given Array and returning the encrypted Strings after encryption with WinDPAPI encryption Service
     * @param pCredentials Array with credentials to encrypt
     * @return Array with encrypted credentials
     * @throws WinAPICallFailedException If Encryption Failed
     * @throws InitializationFailedException If Encryption Instance Initialization Failed
     */
    private String[] encrypt(String[] pCredentials) throws WinAPICallFailedException, InitializationFailedException {
        //Check if WinDPAPI is supported
        if (!WinDPAPI.isPlatformSupported()) {
            throw new UnsupportedOperationException("The Windows Data Protection API (DPAPI) is not available on " + System.getProperty("os.name") + ".");
        }
        //Create Encryption Instance and output Array
        winDPAPI = WinDPAPI.newInstance(WinDPAPI.CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);
        String[] encryptedCredentials = new String[pCredentials.length];
        //For every String in given Array: encrypt using winDPAPI
        for (int x = 0; x < pCredentials.length; x++) {
            byte[] encryptedBytes = winDPAPI.protectData(pCredentials[x].getBytes(UTF_8));
            encryptedCredentials[x] = Base64.getEncoder().encodeToString(encryptedBytes);
        }
        return encryptedCredentials;
    }

    /**
     * Method to decrypt all elements in the given Array and returning the decrypted Strings after decryption with WinDPAPI decryption Service
     * @param pCredentials Array with credentials to decrypt
     * @return Array with decrypted credentials
     * @throws WinAPICallFailedException If Decryption Failed
     * @throws InitializationFailedException If Decryption Instance Initialization Failed
     */
    private String[] decrypt(String[] pCredentials) throws WinAPICallFailedException, InitializationFailedException {
        //Check if WinDPAPI is supported
        if (!WinDPAPI.isPlatformSupported()) {
            throw new UnsupportedOperationException("The Windows Data Protection API (DPAPI) is not available on " + System.getProperty("os.name") + ".");
        }
        //Create Decryption Instance and output Array
        winDPAPI = WinDPAPI.newInstance(WinDPAPI.CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);
        String[] decryptedCredentials = new String[pCredentials.length];
        //For every String in given Array: decrypt using winDPAPI
        for (int x = 0; x < pCredentials.length; x++) {
            byte[] decryptedBytes = Base64.getMimeDecoder().decode(pCredentials[x]);
            decryptedCredentials[x] = new String(winDPAPI.unprotectData(decryptedBytes), UTF_8);
        }
        return decryptedCredentials;
    }

    /**
     * Method to save and encrypt given credentials to json File
     * @param pCredentials Array with credentials to safely store
     * @throws WinAPICallFailedException Error while encrypting credentials
     */
    public void saveCredentials(String[] pCredentials) throws WinAPICallFailedException {
        //Create Arrays and JSON Objects
        JSONObject jsonObject = new JSONObject();
        String[] categories = new String[]{"username", "password", "school", "server"};
        String[] encryptedCredentials;
        //Encrypt credentials using winDPAPI encrypt Method
        try {
            encryptedCredentials = encrypt(pCredentials);
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
        //Create Json Objects and Parser
        JsonParser parser = new JsonParser();
        JsonObject jsonObject;
        //Read Json File
        try {
            jsonObject = (JsonObject) parser.parse(new FileReader(fileLocation));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //Extract credentials from json File and save to Array
        String[] credentials = new String[4];
        credentials[0] = String.valueOf(jsonObject.get("username"));
        credentials[1] = String.valueOf(jsonObject.get("password"));
        credentials[2] = String.valueOf(jsonObject.get("school"));
        credentials[3] = String.valueOf(jsonObject.get("server"));

        //Decrypt credentials using winDPAPI decrypt Method
        try {
            credentials = decrypt(credentials);
        } catch (WinAPICallFailedException | InitializationFailedException e) {
            throw new RuntimeException(e);
        }
        return credentials;
    }
}
