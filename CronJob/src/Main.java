import cron.cronjobs.CornStatsPrinter;
import cron.cronjobs.Test1;
import cron.model.CronJob;
import cron.CronMaster;

public class Main {
    public static void main(String args[]) {
        CronMaster m = new CronMaster();
        m.RegisterCronJob("* * * * *", new Test1());
        m.RegisterCronJob("*/2 * * * *", new CornStatsPrinter(m.stats));
        m.Start();
    }
}
