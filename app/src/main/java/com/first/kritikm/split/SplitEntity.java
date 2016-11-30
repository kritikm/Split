package com.first.kritikm.split;

/**
 * Created by Kritik on 14-Nov-16.
 */

public class SplitEntity
{
    public int eventId;
    public String eventName;
    public String eventDate;
    public String eventLocation;
    public String currency;
    public Double totalAmount;

    public SplitEntity() {}

    public SplitEntity(int _eventId, String _eventName, String _eventDate, String _eventLocation, String _currency, Double _totalAmount)
    {
        eventId = _eventId;
        eventName = _eventName;
        eventDate = _eventDate;
        eventLocation = _eventLocation;
        currency = _currency;
        totalAmount = _totalAmount;
    }

}
