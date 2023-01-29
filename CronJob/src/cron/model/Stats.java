package cron.model;

import cron.model.CronJobStruct;
import cron.model.Time;
import cron.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Stats {
    public static class Count {
        CronJobStruct j;
        int count;
    }

    public static class Stat {
        Time t;
        List<Count> counts = new ArrayList<>();
    }

    public List<Stat> statList = new ArrayList<>();

    public synchronized void AddCronJob(Time t, CronJobStruct j) {
        Stat s = null;
        for (Stat s2 : statList) {
            if (s2.t.year == t.year && s2.t.month == t.month && s2.t.dayOfMonth == t.dayOfMonth) {
                s = s2;
                break;
            }
        }

        if (s == null) {
            s = new Stat();
            s.t = t.Clone();
            statList.add(s);
        }

        for (Count c : s.counts) {
            if (c.j == j) {
                c.count += 1;
                return;
            }
        }
        Count c = new Count();
        c.count = 1;
        c.j = j;
        s.counts.add(c);
        return;
    }

    public synchronized void PrintStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("CronJob Stats:\n");
        for (Stat s : statList) {
            sb.append("[" + s.t.year + "-" + s.t.month + "-" + s.t.dayOfMonth + "]\n");
            Collections.sort(s.counts, new Comparator<Count>(){
                public int compare(Count s1,Count s2){
                    return s1.j.cronJob.GetName().compareTo(s2.j.cronJob.GetName());
                }});
            for (Count c : s.counts)
                sb.append(c.j.cronJob.GetName() + " [" + c.j.originalCronTime + "] count = " + c.count + "\n");
        }
        Util.Println(sb.toString());
    }
}
