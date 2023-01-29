package cron.model;

public class CompiledCronTime {
    public boolean[] minute= new boolean[60];
    public boolean[] hour= new boolean[25];
    public boolean[] dayOfMonth= new boolean[32];
    public boolean[] month= new boolean[13];
    public boolean[] dayOfWeek= new boolean[8];

    public boolean UnionMonthAndWeek = false;
}
