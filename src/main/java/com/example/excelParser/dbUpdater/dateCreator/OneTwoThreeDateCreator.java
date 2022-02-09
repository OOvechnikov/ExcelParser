package com.example.excelParser.dbUpdater.dateCreator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class OneTwoThreeDateCreator implements DateCreator {

    private int counter = 1;
    private final Calendar calendar;

    public OneTwoThreeDateCreator(int year, int month, int day) {
        calendar = new GregorianCalendar(year, month, day);
    }

    @Override
    public Date createDate() {
        Date date = calendar.getTime();
        if (counter < 3) {
            calendar.roll(Calendar.DAY_OF_MONTH, 1);
            counter++;
        } else {
            counter = 1;
            calendar.roll(Calendar.DAY_OF_MONTH, -2);
        }
        return date;
    }
}
