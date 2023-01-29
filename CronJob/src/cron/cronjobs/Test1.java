package cron.cronjobs;

import cron.model.CronJob;
import cron.model.Stats;
import cron.util.Util;

public class Test1 implements CronJob {
    @Override
    public String GetName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void Run() {
        Util.Println("Hi, Test1 here");
    }
}
