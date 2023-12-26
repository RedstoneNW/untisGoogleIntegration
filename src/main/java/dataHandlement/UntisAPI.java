package dataHandlement;

import com.google.api.services.calendar.model.Event;
import org.bytedream.untis4j.Session;
import org.bytedream.untis4j.responseObjects.Timetable;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class UntisAPI {
    /**
     * Clear all added events in weeksToUpdate range
     */
    private static final boolean clearAll = false;

    /**
     *
     * @param args Main requirement
     * @throws IOException Required for API Call
     */
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        //Load Config File Configurations
        Config config = new Config();
        int weeksToUpdate = (int) config.getHowManyWeeksToUpdate();

        LoginDataHandler loginDataHandler = new LoginDataHandler(config);

        //Load credentials
        String[] credentials = loginDataHandler.getCredentials();
        String username = credentials[0];
        String password = credentials[1];
        String school = credentials[2];
        String server = credentials[3];

        //Create Webuntis Session
        Session session = Session.login(username,password,server,school);
        System.out.println("-----------Logged in-----------");
        System.out.println("PersonID: " + session.getInfos().getPersonId());
        System.out.println("-------------------------------");

        //Calculate how many days needs to be updated
        int daysToUpdate = weeksToUpdate *7;
        System.out.println(ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY))));

        daysToUpdate += (int) ChronoUnit.DAYS.between(LocalDate.now().minusDays(1),LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)));

        System.out.println(daysToUpdate);

        //Create Google API
        GoogleCalendarAPI calendar = new GoogleCalendarAPI(config);

        //For every day to update repeated
        for (int x = 0; x < daysToUpdate; x++) {
            //Get Timetable of the day
            Timetable timetable = session.getTimetableFromPersonId(LocalDate.now().plusDays(x), LocalDate.now().plusDays(x), session.getInfos().getPersonId());
            timetable.sortByStartTime();
            List<Event> events = null;
            if (!timetable.isEmpty()) {
                events = calendar.getEvents(timetable.get(0).getDate().toString());
            }

            //Iterate through all subjects of the day
            for (int i = 0; i < timetable.size(); i++) {
                System.out.println("Lesson " + (i+1) + ":");
                //Check if lesson is cancelled
                if (Objects.equals(timetable.get(i).getTeachers().toString(), "[]") || Objects.equals(timetable.get(i).getCode().toString(), "CANCELLED") || clearAll) {
                    System.out.println("CANCELLED");
                    System.out.println(events);
                    //Delete lesson event if it is already added
                    if (events != null) {
                        System.out.println("Sizes: ");
                        System.out.println(events.size());
                        System.out.println(timetable.size());
                        for (int y = 0; y < events.size(); y++) {
                            System.out.println("-" + y + "-");
                            if (events.get(y).getSummary().equals(timetable.get(i).getSubjects().getLongNames().toString())) {
                                calendar.removeEvent(events.get(y).getId());
                                events.remove(events.get(y));
                                System.out.println("Existing class was deleted");
                            }
                        }
                    }
                } else {
                    //Set start and end time
                    String startTime = timetable.get(i).getStartTime().toString();
                    String endTime = timetable.get(i).getEndTime().toString();

                    //Obtain next and current lesson
                    String nextLesson = "UNDEFINED";
                    String currentLesson = timetable.get(i).getSubjects().getIds().toString();

                    if (i < timetable.size() - 1) {
                        nextLesson = timetable.get(i+1).getSubjects().getIds().toString();
                    }
                    System.out.println(currentLesson);
                    System.out.println(nextLesson);

                    //Check for single or double lesson
                    if (currentLesson.equals(nextLesson)) {
                        endTime = timetable.get(i+1).getEndTime().toString();
                        i++;
                    }

                    String currentDate = timetable.get(i).getDate().toString();

                    //Check if lesson event is already added
                    if (!eventExists(timetable,i,events)) {
                        System.out.println("Event not existing");
                        //Check if exam is to be added
                        if (!Objects.equals(timetable.get(i).getSubjects().getLongNames().toString(), "[]")) {
                            calendar.addEvent(
                                    timetable.get(i).getSubjects().getLongNames().toString(),
                                    timetable.get(i).getRooms().getNames().toString(),
                                    timetable.get(i).getTeachers().getLongNames().toString(),
                                    currentDate,
                                    currentDate,
                                    startTime,
                                    endTime
                            );
                        } else {
                            if (events != null) {
                                Iterator<Event> iterator = events.iterator();
                                while (iterator.hasNext()) {
                                    Event event = iterator.next();
                                    if (event.getSummary().equals("Klausur")) {
                                        calendar.removeEvent(event.getId());
                                        iterator.remove(); // Use iterator's remove method
                                    }
                                }
                            }
                            calendar.addEvent(
                                    "Klausur",
                                    timetable.get(i).getRooms().getNames().toString(),
                                    timetable.get(i).getTeachers().getLongNames().toString(),
                                    currentDate,
                                    currentDate,
                                    startTime,
                                    timetable.get(i).getEndTime().toString()
                                    );
                        }
                    }
                }
            }
        }
        System.out.println("-----------Logged out-----------");
        session.logout();
    }

    /**
     * Method to check if event already exists
     * @param timetable Untis timetable to check
     * @param currentLesson Current Lesson to check in timetable
     * @return If event exists
     */
    public static boolean eventExists(Timetable timetable, int currentLesson, List<Event> pEvents) {
        //Get all events from given Day
        System.out.println("----Checking for existing Events----");
        boolean isExisting = false;
        //Check if event exists
        if (pEvents != null) {
            for (int i = 0; i < pEvents.size(); i++) {
                System.out.println("-" + i + "-");
                System.out.println(timetable.get(currentLesson).getSubjects().getLongNames().toString());
                System.out.println(pEvents.get(i).getSummary());
                System.out.println("---");
                if (pEvents.get(i).getSummary().equals(timetable.get(currentLesson).getSubjects().getLongNames().toString())) {
                    isExisting = true;
                }
            }
        }
        return isExisting;
    }
}
