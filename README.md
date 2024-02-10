# untisGoogleIntegration
This program syncs the timetable from Untis to the Google Calendar using the Untis Timetable API as well as the Google Calendar API


# Config
The installation wizard automatically sets the necessary config settings. If you wish to change them manually, you can do so in the conf.toml file.
There are 5 configs that can only be changed manually and not through the app itself.

**untisCredentialPath:** Here you can specify where your encrypted credentials should be saved

**googleCredentialsFile:** Here you can specify where your encrypted Google account secrets should be saved

**googleTokensLocation:** Here the Google Authentication Tokens are saved in the given Path

**logsFileLocation:** This is the location of the Log File

**howManyWeeksToUpdate:** Here you can change how many weeks should be updated in advance. Be aware that increasing that number drastically increases the time the program needs to complete syncing
