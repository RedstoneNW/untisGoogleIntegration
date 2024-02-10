package dataHandlement;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private boolean acceptedPrivacyPolicy;
    private boolean givenUntisCredentials;
    private String untisCredentialsFile;
    private String googleCredentialsFile;
    private String googleTokensLocation;
    private String logsFileLocation;
    private String calendarToStore;
    private Long howManyWeeksToUpdate;
    private final Path FILE_LOCATION = Paths.get("./conf.toml");

    public Config() throws IOException {
        if (!Files.exists(FILE_LOCATION)) {
            acceptedPrivacyPolicy = false;
            givenUntisCredentials = false;
            untisCredentialsFile = "./credentials/untis.json";
            googleCredentialsFile = "./credentials/google.json";
            googleTokensLocation = "credentials/tokens";
            logsFileLocation = "./log.txt";
            calendarToStore = "primary";
            howManyWeeksToUpdate = 3L;
            return;
        }
        TomlParseResult conf = Toml.parse(FILE_LOCATION);
        if (conf.contains("acceptedPrivacyPolicy")) {
            acceptedPrivacyPolicy = Boolean.TRUE.equals(conf.getBoolean("acceptedPrivacyPolicy"));
        } else {
            acceptedPrivacyPolicy = false;
        }
        if (conf.contains("givenUntisCredentials")) {
            givenUntisCredentials = Boolean.TRUE.equals(conf.getBoolean("givenUntisCredentials"));
        } else {
            givenUntisCredentials = false;
        }
        if (conf.contains("untisCredentialsFile")) {
            untisCredentialsFile = conf.getString("untisCredentialsFile");
            if (untisCredentialsFile != null && untisCredentialsFile.isEmpty()) {
                untisCredentialsFile = "./credentials/untis.json";
            }
        } else {
            untisCredentialsFile = "./credentials/untis.json";
        }
        if (conf.contains("googleCredentialsFile")) {
            googleCredentialsFile = conf.getString("googleCredentialsFile");
            if (googleCredentialsFile != null && googleCredentialsFile.isEmpty()) {
                googleCredentialsFile = "./credentials/google.json";
            }
        } else {
            googleCredentialsFile = "./credentials/google.json";
        }
        if (conf.contains("googleTokensLocation")) {
            googleTokensLocation = conf.getString("googleTokensLocation");
            if (googleTokensLocation != null && googleTokensLocation.isEmpty()) {
                googleTokensLocation = "credentials/tokens";
            }
        } else {
            googleTokensLocation = "credentials/tokens";
        }
        if (conf.contains("logsFileLocation")) {
            logsFileLocation = conf.getString("logsFileLocation");
            if (logsFileLocation != null && logsFileLocation.isEmpty()) {
                logsFileLocation = "./log.txt";
            }
        } else {
            logsFileLocation = "./log.txt";
        }
        if (conf.contains("calendarToStore")) {
            calendarToStore = conf.getString("calendarToStore");
            if (calendarToStore != null && calendarToStore.isEmpty()) {
                calendarToStore = "primary";
            }
        } else {
            calendarToStore = "primary";
        }
        if (conf.contains("howManyWeeksToUpdate")) {
            howManyWeeksToUpdate = conf.getLong("howManyWeeksToUpdate");
            if (howManyWeeksToUpdate == null) {
                howManyWeeksToUpdate = 3L;
            }
        } else {
            howManyWeeksToUpdate = 3L;
        }
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

