package cron.ut;

import cron.CronMaster;
import cron.model.CompiledCronTime;
import cron.model.Time;

public class Ut {
    public static void Check(boolean b) {
        if (!b) {
            throw new Error("Check failed");
        }
    }

    public static void UtGetCompiledCronTime() {
        CronMaster m = new CronMaster();
        Check(m.GetCompiledCronTime("* * * * ") == null);
        Check(m.GetCompiledCronTime("* */2, 3 * * *") == null);
        Check(m.GetCompiledCronTime("* * * * * *") == null);
        Check(m.GetCompiledCronTime("* a * * *") == null);
        Check(m.GetCompiledCronTime("* 25 * * *") == null);
        Check(m.GetCompiledCronTime("* * * * *") != null);
        Check(m.GetCompiledCronTime("  * * * * * ") != null);
        Check(m.GetCompiledCronTime("* */2 1,2 3-5 7") != null);

        CompiledCronTime t = m.GetCompiledCronTime("* * * * *");
        Check(t.UnionMonthAndWeek == false);
        t = m.GetCompiledCronTime("* * 1 * *");
        Check(t.UnionMonthAndWeek == false);
        t = m.GetCompiledCronTime("* * * * 1");
        Check(t.UnionMonthAndWeek == false);
        t = m.GetCompiledCronTime("* * 1 * 1");
        Check(t.UnionMonthAndWeek == true);
        t = m.GetCompiledCronTime("* * */5,1 * 1");
        Check(t.UnionMonthAndWeek == false);

        t = m.GetCompiledCronTime("* */2 1,2 3-5 7");
        for (int i = 0; i < t.minute.length; i++) {
            Check(t.minute[i] == true);
        }
        for (int i = 0; i < t.hour.length; i++) {
            if (i % 2 == 0)
                Check(t.hour[i] == true);
            else
                Check(t.hour[i] == false);
        }
        for (int i = 0; i < t.dayOfMonth.length; i++) {
            if (i == 1 || i == 2)
                Check(t.dayOfMonth[i] == true);
            else
                Check(t.dayOfMonth[i] == false);
        }
        for (int i = 0; i < t.month.length; i++) {
            if (3 <= i && i <= 5)
                Check(t.month[i] == true);
            else
                Check(t.month[i] == false);
        }
        for (int i = 0; i < t.dayOfWeek.length; i++) {
            if (i == 7)
                Check(t.dayOfWeek[i] == true);
            else
                Check(t.dayOfWeek[i] == false);
        }
    }

    public static void UtCompareTime() {
        CronMaster m = new CronMaster();
        Time t = new Time();
        t.dayOfMonth = 1;
        t.dayOfWeek = 1;
        t.hour = 0;
        t.minute = 0;
        t.month = 1;
        Check(m.CompareTime(t, m.GetCompiledCronTime("* * * * *")) == true);

        t.dayOfMonth = 1;
        t.dayOfWeek = 1;
        t.hour = 0;
        t.minute = 0;
        t.month = 4;
        Check(m.CompareTime(t, m.GetCompiledCronTime("* * * 3-5 *")) == true);

        t.dayOfMonth = 1;
        t.dayOfWeek = 7;
        t.hour = 0;
        t.minute = 0;
        t.month = 1;
        Check(m.CompareTime(t, m.GetCompiledCronTime("0 0,2 * * 7")) == true);

        t.dayOfMonth = 1;
        t.dayOfWeek = 7;
        t.hour = 0;
        t.minute = 0;
        t.month = 1;
        Check(m.CompareTime(t, m.GetCompiledCronTime("XXX")) == false);

        t.dayOfMonth = 2;
        t.dayOfWeek = 3;
        t.hour = 0;
        t.minute = 0;
        t.month = 1;
        Check(m.CompareTime(t, m.GetCompiledCronTime("0 0 */3 * */3")) == false);

        t.dayOfMonth = 3;
        t.dayOfWeek = 3;
        t.hour = 0;
        t.minute = 0;
        t.month = 1;
        Check(m.CompareTime(t, m.GetCompiledCronTime("0 0 */3 * */3")) == true);

        t.dayOfMonth = 3;
        t.dayOfWeek = 3;
        t.hour = 0;
        t.minute = 0;
        t.month = 1;
        Check(m.CompareTime(t, m.GetCompiledCronTime("0 0 */3 * */4")) == false);
    }

    public static void Ut() {
        UtGetCompiledCronTime();
        UtCompareTime();
    }
}
