package cron.cronjobs;

import cron.model.*;

public class CornStatsPrinter implements  CronJob {
    Stats s;
    public CornStatsPrinter(Stats s) {
        this.s = s;
    }

    @Override
    public String GetName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void Run() {
        s.PrintStats();
    }
}
