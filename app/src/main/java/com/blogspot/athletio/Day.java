package com.blogspot.athletio;


import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by tanvirf on 8/25/17.
 */

public class Day {
    int day;
    int month;
    int year;

    public Day(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Day() {
        this.day = Calendar.getInstance().get(Calendar.DATE);
        this.month=Calendar.getInstance().get(Calendar.MONTH);
        this.year=Calendar.getInstance().get(Calendar.YEAR);
    }
    public Day(String str) {
        JsonObjectParser jsonObjectParser=new JsonObjectParser(str);
        this.day=jsonObjectParser.getInt("day");
        this.month=jsonObjectParser.getInt("month");
        this.year=jsonObjectParser.getInt("year");
    }

    @Override
    public String toString() {
        return "{" +
                "day=" + day +
                ",month=" + month +
                ",year=" + year +
                '}';
    }


}
