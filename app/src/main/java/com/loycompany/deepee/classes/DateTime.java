package com.loycompany.deepee.classes;

import androidx.annotation.NonNull;

import java.util.List;

public class DateTime {
    int sec = 0;
    int min = 0;
    int hour = 0;

    int year = 0;
    int month = 0;
    int day = 0;

    public DateTime() {
    }

    public DateTime(int sec, int min, int hour, int year, int month, int day) {
        this.sec = sec;
        this.min = min;
        this.hour = hour;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @NonNull
    @Override
    public String toString(){
        return day + "-" + month + "-" + year + "(" + hour + "-" + min + "-" + sec;
    }


    public static DateTime parseData(String data){
        DateTime dataTime = new DateTime();

        List<String> list = DataParser.javaexplode("(", data);

        String p1 = list.get(0);
        String p2 = list.get(1);

        List<String> dates = DataParser.javaexplode("-", p1);

        dataTime.day = Integer.parseInt(dates.get(0));
        dataTime.month = Integer.parseInt(dates.get(1));
        dataTime.year = Integer.parseInt(dates.get(2));

        List<String> times = DataParser.javaexplode("-", p2);

        dataTime.sec = Integer.parseInt(times.get(0));
        dataTime.min = Integer.parseInt(times.get(1));
        dataTime.hour = Integer.parseInt(times.get(2));

        return dataTime;
    }
}
