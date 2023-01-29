package cron.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static void Println(String s) {
        System.out.println("[cron][" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] " + s);
    }

    public static void SleepNoThrow(int milli) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Println("Sleep exception: " + e.toString());
        }
    }
}
