package cron;

import cron.model.*;
import cron.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// [minute] [hour] [day-of-month] [month] [day-of-week]
// * * * * *
// 0 0 1 * *     ; At 00:00 on day-of-month 1
// 0 0 3,*/10 * *; At 00:00 on day-of-month 3 and every 10th day-of-month (e.g. 10, 20, 30)
// 0 0 * * 1     ; At 00:00 on Monday
// 0 0 1,3-5,*/4 * *  ; day-of-month 1 + day-of-month 3 to day-of-month 5 + every 4th of month
// If the day-of-month or day-of-week part starts with a *, they form an intersection. Otherwise they form a union
// 0 0 3 * 1     ; 3rd day of the month and on Monday (union)
// 0 0 */2 * 1   ; At 00:00 on every 2nd day-of-month if it's on Monday (intersection)

public class CronMaster {
    boolean end = false;
    List<CronJobStruct> installedCronJob = new ArrayList<>();
    Time prevTime = null;
    public Stats stats = new Stats();

    public boolean CompareTime(Time now, CompiledCronTime cronTime) {
        if (cronTime == null)
            return false;

        boolean monthMatched = false;
        if (cronTime.minute[now.minute] == true && cronTime.hour[now.hour] == true && cronTime.dayOfMonth[now.dayOfMonth] == true && cronTime.month[now.month] == true)
            monthMatched = true;

        boolean weekMatched = false;
        if (cronTime.minute[now.minute] == true && cronTime.hour[now.hour] == true && cronTime.dayOfWeek[now.dayOfWeek] == true)
            weekMatched = true;

        if (cronTime.UnionMonthAndWeek)
            return monthMatched || weekMatched;
        else
            return monthMatched && weekMatched;
    }

    public CompiledCronTime GetCompiledCronTime(String cronTime) {
        List<String> tokens = Arrays.stream(cronTime.split(" ")).filter(s -> !s.trim().isEmpty()).map(s -> s.trim()).collect(Collectors.toList());

        if (tokens.size() != 5)
            return null;

        CompiledCronTime ret = new CompiledCronTime();

        if (tokens.get(2).startsWith("*") || tokens.get(4).startsWith("*")) {
            ret.UnionMonthAndWeek = false;
        } else {
            ret.UnionMonthAndWeek = true;
        }


        for (int i = 0; i < 5; i++) {
            boolean flags[] = new boolean[0];
            int max = -1;
            if (i == 0) {
                flags = ret.minute;
            } else if (i == 1) {
                flags = ret.hour;
            }else if (i == 2) {
                flags = ret.dayOfMonth;
            }else if (i == 3) {
                flags = ret.month;
            }else if (i == 4) {
                flags = ret.dayOfWeek;
            }

            String time2 = tokens.get(i);
            List<String> tokens2 = Arrays.stream(time2.split(",")).filter(s -> !s.trim().isEmpty()).map(s -> s.trim()).collect(Collectors.toList());
            for (String token : tokens2) {
                if (token.contains("/")) {
                    if (token.charAt(0) != '*')
                        return null;
                    String divisorString = token.substring(2);
                    try {
                        int divisor = Integer.parseInt(divisorString);
                        if (divisor <= 0)
                            return null;
                        for (int j = 0; j < flags.length; j++) {
                            if (j % divisor == 0) {
                                flags[j] = true;
                            }
                        }
                    } catch (NumberFormatException e) {
                        return null;
                    }
                } else if (token.contains("-")) {
                    try {
                        int dash = token.indexOf('-');
                        int begin = Integer.parseInt(token.substring(0, dash));
                        int end = Integer.parseInt(token.substring(dash + 1));
                        if (begin < 0 || end < 0 || end < begin || flags.length <= begin || flags.length <= end)
                            return null;

                        for (int j = begin; j <= end; j++) {
                            flags[j] = true;
                        }
                    } catch (NumberFormatException e) {
                        return null;
                    }
                } else if (token.equals("*")) {
                    for (int j = 0; j < flags.length; j++) {
                        flags[j] = true;
                    }
                } else {
                    try {
                        int index = Integer.parseInt(token);
                        if (index < 0 || flags.length <= index)
                            return null;

                        flags[index] = true;
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
        }

        return ret;
    }

    public boolean RegisterCronJob(String time, CronJob cronJob) {
        CompiledCronTime t = GetCompiledCronTime(time);
        if (t == null) {
            Util.Println("Registering " + cronJob.GetName() + " failed!");
            return false;
        }

        CronJobStruct j = new CronJobStruct();
        j.originalCronTime = time;
        j.compiledcronTime = t;
        j.cronJob = cronJob;
        installedCronJob.add(j);
        Util.Println("Registering " + cronJob.GetName() + " ok");
        return true;
    }

    public void End() {
        end = true;
    }

    public void Start() {
        prevTime = Time.GetNow();
        end = false;
        while (!end) {
            Time now = Time.GetNow();
            if (prevTime != null && prevTime.dayOfMonth == now.dayOfMonth && prevTime.hour == now.hour && prevTime.minute == now.minute) {
                Util.SleepNoThrow(1000);
                continue;
            }

            prevTime = now;

            for (CronJobStruct j : installedCronJob) {
                if (CompareTime(now, j.compiledcronTime) == true) {
                    Run(now, j);
                }
            }
        }
    }

    public void Run(Time t, CronJobStruct job) {
        new Thread() {
            public void run(){
                stats.AddCronJob(t, job);
                job.cronJob.Run();
            }
        }.start();
    }
}
