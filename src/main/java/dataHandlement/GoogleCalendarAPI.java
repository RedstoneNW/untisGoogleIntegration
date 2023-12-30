package dataHandlement;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class GoogleCalendarAPI {
    /**
     * Private variables declaration
     * <p></p>
     * Untis Google Calendar Integration
     */
    private static final String APPLICATION_NAME = "Untis Google Calendar Integration";
    /**
     * Global instance of the JSON Factory
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens
     */
    private static String TOKENS_DIRECTORY_PATH = "credentials/tokens";
    /**
     * Global Instances of the scopes required
     */
    private static final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR);
    /**
     * Path to credentials file
     */
    private static String CREDENTIALS_FILE_PATH = "./credentials/google.json";

    private static String CALENDAR_ID = "c543f2e06e087d1188af906633a81116848531b716162683b0dfe1e808dc22b0@group.calendar.google.com";

    private final NetHttpTransport HTTP_TRANSPORT;
    private Calendar service;

    /**
     * Constructor for GoogleCalendarAPI
     *
     * @throws IOException              If the credential file cannot be read/found
     * @throws GeneralSecurityException For API Service
     */
    public GoogleCalendarAPI(Config config) throws IOException, GeneralSecurityException{
            TOKENS_DIRECTORY_PATH = config.getGoogleTokensLocation();
            CREDENTIALS_FILE_PATH = config.getGoogleCredentialsFile();
            CALENDAR_ID = config.getCalendarToStore();

            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            System.out.println("Service built");
    }

    /**
     * Creates an authorized Credential Object
     *
     * @param HTTP_TRANSPORT The network HTTP Transport
     * @return An authorized Credential Object
     * @throws IOException If the credential file cannot be read/found
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        GoogleAuthorizationCodeFlow flow = buildAuthorizationFlow(HTTP_TRANSPORT);
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        try {
            // Attempt to get credentials
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (TokenResponseException e) {
            handleTokenResponseException(flow, receiver, e);
        }

        // Rethrow the exception as a RuntimeException
        throw new RuntimeException("Google API Token error");
    }

    private GoogleAuthorizationCodeFlow buildAuthorizationFlow(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization
        return new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
    }

    private void handleTokenResponseException(GoogleAuthorizationCodeFlow flow, LocalServerReceiver receiver, TokenResponseException e) throws IOException {
        // Handle the TokenResponseException here
        if (e.getStatusCode() == 400) {
            System.out.println("Error: 400 Bad Request");
            System.out.println("Error Content: " + e.getContent());

            // Attempt to refresh the token
            System.out.println("Attempting to refresh the token...");
            Credential credential = refreshAccessToken(flow);
            if (credential != null && credential.getAccessToken() != null) {
                System.out.println("Token refreshed successfully.");
                return;
            }

            // If token refresh fails, delete the existing token and prompt user to reauthorize
            System.out.println("Deleting existing token...");
            flow.getCredentialDataStore().delete("user");

            // Prompt the user to reauthorize
            new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
            System.out.println("Token reauthorized successfully.");
        } else {
            System.out.println("TokenResponseException: " + e.getMessage());
            // Handle other TokenResponseException cases
        }
    }

    private Credential refreshAccessToken(GoogleAuthorizationCodeFlow flow) {
        try {
            return flow.loadCredential("user").setAccessToken(null);
        } catch (IOException e) {
            System.out.println("Error refreshing token: " + e.getMessage());
            return null;
        }
    }

    private void renewToken() throws IOException {
        GoogleAuthorizationCodeFlow flow = buildAuthorizationFlow(HTTP_TRANSPORT);
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        refreshAccessToken(flow);
        Credential credential = getCredentials(HTTP_TRANSPORT);
        if (credential.getAccessToken() != null) {
            System.out.println("Token renewed successfully.");
        } else {
            // If token refresh fails, delete the existing token and prompt the user to reauthorize
            System.out.println("Deleting existing token...");
            flow.getCredentialDataStore().delete("user");

            // Prompt the user to reauthorize
            new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
            System.out.println("Token reauthorized successfully.");
        }
        service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Method to get all Events on specific date
     *
     * @param date The date of which the events should be returned
     * @return A List of all events of the date, or an empty list if there are no events or date is null
     * @throws IOException              For API Service
     */
    public List<Event> getEvents(String date) throws IOException {
        if (date != null) {
            //Timezone handler
            TimeZone tz = TimeZone.getTimeZone("Europe/Berlin");
            long now = System.currentTimeMillis();
            System.out.println(tz.getOffset(now));

            //Convert Start & End time of day to DateTime format
            String startDate = date + "T" + "00:00" + ":00+0" + (tz.getOffset(now) / 1000 / 60 / 60) + ":00";
            String endDate = date + "T" + "23:59" + ":00+0" + (tz.getOffset(now) / 1000 / 60 / 60) + ":00";

            DateTime startTime = DateTime.parseRfc3339(startDate);
            DateTime endTime = DateTime.parseRfc3339(endDate);

            List<Event> items;

            //Get all Events from date
            try {
                Events events = getServiceEvents(startTime,endTime);
                items = events.getItems();
            } catch (TokenResponseException e) {
                renewToken();
                Events events = getServiceEvents(startTime,endTime);
                items = events.getItems();
            }
            //Return all items
            if (!items.isEmpty()) {
                return items;
            }
        }
        return Collections.emptyList();
    }

    private Events getServiceEvents(DateTime startTime, DateTime endTime) throws IOException {
        return service.events().list(CALENDAR_ID)
                .setTimeMin(startTime)
                .setTimeMax(endTime)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
    }

    /**
     * Adds an event to the primary calendar with the provided features
     *
     * @param name        Name of event
     * @param location    Location of event
     * @param description Description of event
     * @param pStartDate  Date on which the event starts
     * @param pStartTime  Time on which the event starts
     * @param pEndDate    Date on which the event ends
     * @param pEndTime    Time on which the event ends
     * @throws IOException              For API service
     */
    public void addEvent(String name, String location, String description, String pStartDate, String pEndDate, String pStartTime, String pEndTime) throws IOException {
        //Timezone handler
        TimeZone tz = TimeZone.getTimeZone("Europe/Berlin");
        long now = System.currentTimeMillis();
        System.out.println(tz.getOffset(now));
        System.out.println(pStartTime);

        //Convert Start & End time to DateTime format
        String startDate = pStartDate + "T" + pStartTime + ":00.000+0" + ((tz.getOffset(now) / 1000 / 60 / 60)) + ":00";
        String endDate = pEndDate + "T" + pEndTime + ":00.000+0" + ((tz.getOffset(now) / 1000 / 60 / 60)) + ":00";

        System.out.println(startDate);

        DateTime startTime = DateTime.parseRfc3339(startDate);
        DateTime endTime = DateTime.parseRfc3339(endDate);

        System.out.println(startTime);

        // Add event to calendar
        Event event = new Event()
                .setColorId("4")
                .setSummary(name)
                .setLocation(location)
                .setDescription(description);
        //Set duration of created event
        EventDateTime start = new EventDateTime()
                .setDateTime(startTime);
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(endTime);
        event.setEnd(end);

        //Turn off reminders for event
        Event.Reminders reminders = new Event.Reminders().setUseDefault(false);
        event.setReminders(reminders);

        //Add event to primary calendar
        event = service.events().insert(CALENDAR_ID, event).execute();
        System.out.println("Event add to calendar: %s\n" + event.getHtmlLink());
    }

    /**
     * Method to remove events from primary calendar
     *
     * @param id id of event to remove
     * @throws IOException              For API service
     */
    public void removeEvent(String id) throws IOException {
        //Remove event
        service.events().delete(CALENDAR_ID, id).execute();
    }
}