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

    @NonNull
    @Override
    public String toString(){
        return String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year) + "(" + String.valueOf(hour) + "-" + String.valueOf(min) + "-" + String.valueOf(sec);
    }

    public void parseData(String data){
        List<String> list = DataParser.javaexplode("(", data);

        String p1 = list.get(0);
        String p2 = list.get(1);

        List<String> dates = DataParser.javaexplode("-", p1);

        this.day = Integer.parseInt(dates.get(0));
        this.month = Integer.parseInt(dates.get(1));
        this.year = Integer.parseInt(dates.get(2));

        List<String> times = DataParser.javaexplode("-", p2);

        this.sec = Integer.parseInt(times.get(0));
        this.min = Integer.parseInt(times.get(1));
        this.hour = Integer.parseInt(times.get(2));
    }
}
