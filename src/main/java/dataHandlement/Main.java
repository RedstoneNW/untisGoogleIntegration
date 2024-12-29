package dataHandlement;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        Config config = new Config();
        UntisAPI untisAPI = new UntisAPI(config);
        untisAPI.syncCalendar();
    }
}
