package dataHandlement;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Config {
    private boolean acceptedPrivacyPolicy;
    private boolean givenUntisCredentials;
    private String untisCredentialsFile;
    private String googleCredentialsFile;
    private String googleTokensLocation;
    private String logsFileLocation;
    private String calendarToStore;
    private Long howManyWeeksToUpdate;
    private String AESKEY;
    private boolean useOAuth;
    private String googleServiceAccountJson;
    private final Path FILE_LOCATION = Paths.get("./conf.toml");

    public Config() throws IOException {
        if (!Files.exists(FILE_LOCATION)) {
            acceptedPrivacyPolicy = getEnvVal("acceptedPrivacyPolicy", false, Optional.empty());
            givenUntisCredentials = getEnvVal("givenUntisCredentials", false, Optional.empty());
            untisCredentialsFile = getEnvVal("untisCredentialsFile", "./credentials/untis.json", Optional.empty());
            googleCredentialsFile = getEnvVal("googleCredentialsFile", "./credentials/google.json", Optional.empty());
            googleTokensLocation = getEnvVal("googleTokensLocation", "credentials/tokens", Optional.empty());
            logsFileLocation = getEnvVal("logsFileLocation", "./log.txt", Optional.empty());
            calendarToStore = getEnvVal("calendarToStore", "primary", Optional.empty());
            howManyWeeksToUpdate = getEnvVal("howManyWeeksToUpdate", 3L, Optional.empty());
            AESKEY = getEnvVal("AESKEY", "", Optional.empty());
            useOAuth = getEnvVal("useOAuth",true,Optional.empty());
            googleServiceAccountJson = getEnvVal("googleServiceAccountJson", "", Optional.empty());
            return;
        }
        TomlParseResult conf = Toml.parse(FILE_LOCATION);
        acceptedPrivacyPolicy = getEnvVal("acceptedPrivacyPolicy", false, Optional.of(conf));
        givenUntisCredentials = getEnvVal("givenUntisCredentials", false, Optional.of(conf));
        untisCredentialsFile = getEnvVal("untisCredentialsFile", "./credentials/untis.json", Optional.of(conf));
        googleCredentialsFile = getEnvVal("googleCredentialsFile", "./credentials/google.json", Optional.of(conf));
        googleTokensLocation = getEnvVal("googleTokensLocation", "credentials/tokens", Optional.of(conf));
        logsFileLocation = getEnvVal("logsFileLocation", "./log.txt", Optional.of(conf));
        calendarToStore = getEnvVal("calendarToStore", "primary", Optional.of(conf));
        howManyWeeksToUpdate = getEnvVal("howManyWeeksToUpdate", 3L, Optional.of(conf));
        AESKEY = getEnvVal("AESKEY", "", Optional.of(conf));
        useOAuth = getEnvVal("useOAuth", true, Optional.of(conf));
        googleServiceAccountJson = getEnvVal("googleServiceAccountJson", "", Optional.of(conf));
    }

    private <ContentType> ContentType getConfigVal(TomlParseResult conf, String key, ContentType defaultValue) {
        if (conf.contains(key)) {
            System.out.println(key + "is in Conf");
            Object configVal = conf.get(key);
            if (configVal != null) {
                if (defaultValue instanceof String) {
                    return (ContentType) configVal;
                } else if (defaultValue instanceof Long) {
                    try {
                        return (ContentType) configVal;
                    } catch (NumberFormatException e) {
                        return defaultValue;
                    }
                } else if (defaultValue instanceof Boolean) {
                    return (ContentType) configVal;
                }
            }
        }
        return defaultValue;
    }

    private <ContentType> ContentType getEnvVal(String key, ContentType defaultValue, Optional<TomlParseResult> conf) {
        String value = System.getenv("UNTISGOOGLESYNC_" + key);
        if (value == null || value.isEmpty()) {
            if (conf.isPresent()) {
                return getConfigVal(conf.get(), key, defaultValue);
            } else return defaultValue;
        }

        if (defaultValue instanceof String){
            return (ContentType) value;
        } else if (defaultValue instanceof Long) {
            try {
                return (ContentType) Long.valueOf(value);
            } catch (NumberFormatException e) {
                if (conf.isPresent()) {
                    return getConfigVal(conf.get(), key, defaultValue);
                } else return defaultValue;
            }
        } else if (defaultValue instanceof Boolean) {
            return (ContentType) Boolean.valueOf(value);
        }
        if (conf.isPresent()) {
            return getConfigVal(conf.get(), key, defaultValue);
        } else return defaultValue;
    }

    public Path getConfigFile_Location() {
        return FILE_LOCATION;
    }

    public boolean hasAcceptedPrivacyPolicy() {
        return acceptedPrivacyPolicy;
    }

    public boolean hasGivenUntisCredentials() {
        return givenUntisCredentials;
    }

    public String getUntisCredentialsFile() {
        return untisCredentialsFile;
    }

    public String getGoogleCredentialsFile() {
        return googleCredentialsFile;
    }

    public String getGoogleTokensLocation() {
        return googleTokensLocation;
    }

    public String getLogsFileLocation() {
        return logsFileLocation;
    }

    public String getCalendarToStore() {
        return calendarToStore;
    }

    public long getHowManyWeeksToUpdate() {
        return howManyWeeksToUpdate;
    }

    public String getAESKEY() {
        return AESKEY;
    }

    public boolean isUseOAuth() {
        return useOAuth;
    }

    public String getGoogleServiceAccountJson() {
        return googleServiceAccountJson;
    }

    public void setCalendarToStore(String newCalendarToStore) {
        File config = new File(String.valueOf(FILE_LOCATION));
        List<String> lines = new ArrayList<>();
        try {
            // Read the file
            BufferedReader reader = new BufferedReader(new FileReader(config));
            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                // If the line contains "calendarToStore", replace it
                if (currentLine.trim().contains("calendarToStore")) {
                    currentLine = "calendarToStore = '" + newCalendarToStore + "'";
                }
                lines.add(currentLine);
            }
            reader.close();

            // Write the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(config));
            for (String line : lines) {
                writer.write(line + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

