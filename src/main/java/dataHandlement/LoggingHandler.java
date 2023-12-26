package dataHandlement;

import gui.StartPage;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LoggingHandler {

    private final List<StartPage> registeredOutputs = new CopyOnWriteArrayList<>();

    private final String logFileLoc;

    public LoggingHandler(Config config) {
        logFileLoc = config.getLogsFileLocation();

        System.setOut(new PrintStream(System.out) {
            public void println(String s) {
                addLog(s);
            }
        });
    }
    public void addLog(String pLogentry) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String logentry = "[" + timestamp + "] " + pLogentry;
        try {
            FileWriter fw = new FileWriter(logFileLoc, true); //the true will append the new data
            fw.write(logentry + "\n");//appends the string to the file
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
        StartPage[] registeredOutputArrays = new StartPage[0];
        registeredOutputArrays = registeredOutputs.toArray(registeredOutputArrays);

        for (StartPage registeredOutputArray : registeredOutputArrays) {
            registeredOutputArray.addLog(logentry);
        }

    }

    public void registerLogOut(StartPage pStartPage) {
        if (pStartPage != null) {
            registeredOutputs.add(pStartPage);
        }
    }

    public List<String> getLogs() {
        List<String> logs = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("./log.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            logs = null;
        }
        return logs;
    }
}
