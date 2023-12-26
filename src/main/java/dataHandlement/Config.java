package dataHandlement;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    private boolean acceptedPrivacyPolicy;
    private boolean givenUntisCredentials;
    private String untisCredentialsFile;
    private String googleCredentialsFile;
    private String googleTokensLocation;
    private String logsFileLocation;
    private String calendarToStore;
    private Long howManyWeeksToUpdate;

    private static final Path FILE_LOCATION = Paths.get("./conf.toml");

    public Config() throws IOException {
        if (!Files.exists(FILE_LOCATION)) {
            return;
        }
        TomlParseResult conf = Toml.parse(FILE_LOCATION);
        if (conf.contains("acceptedPrivacyPolicy")) {
            acceptedPrivacyPolicy = Boolean.TRUE.equals(conf.getBoolean("acceptedPrivacyPolicy"));
        }
        if (conf.contains("givenUntisCredentials")) {
            givenUntisCredentials = Boolean.TRUE.equals(conf.getBoolean("givenUntisCredentials"));
        }
        if (conf.contains("untisCredentialsFile")) {
            untisCredentialsFile = conf.getString("untisCredentialsFile");
        }
        if (conf.contains("googleCredentialsFile")) {
            googleCredentialsFile = conf.getString("googleCredentialsFile");
        }
        if (conf.contains("googleTokensLocation")) {
            googleTokensLocation = conf.getString("googleTokensLocation");
        }
        if (conf.contains("logsFileLocation")) {
            logsFileLocation = conf.getString("logsFileLocation");
        }
        if (conf.contains("calendarToStore")) {
            calendarToStore = conf.getString("calendarToStore");
        }
        if (conf.contains("howManyWeeksToUpdate")) {
            howManyWeeksToUpdate = conf.getLong("howManyWeeksToUpdate");
        }
    }

    public boolean isAcceptedPrivacyPolicy() {
        return acceptedPrivacyPolicy;
    }

    public boolean isGivenUntisCredentials() {
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
}

