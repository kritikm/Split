package com.first.kritikm.split;

/**
 * Created by Kritik on 16-Nov-16.
 */

public class EaterEntity
{
    public int eventId;
    public String eaterName;
    public double eaterAte;
    public double eaterPaid;

    public EaterEntity(){}

    public EaterEntity(int toEventId, String toEaterName, double toEaterAte, double toEaterPaid)
    {
        eventId = toEventId;
        eaterName = toEaterName;
        eaterAte = toEaterAte;
        eaterPaid = toEaterPaid;
    }
}
