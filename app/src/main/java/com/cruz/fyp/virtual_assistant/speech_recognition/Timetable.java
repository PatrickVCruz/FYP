package com.cruz.fyp.virtual_assistant.speech_recognition;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Timetable extends AsyncTask<String,Void,ArrayList<ArrayList<String[]>>> {

    private String idNumber;
    private HashMap<String,Integer> daysOfWeek;
    private int date;

    int getDate() {
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    Timetable(String idNumber, String theDay) {
        this.idNumber = idNumber;
        daysOfWeek = new HashMap<>();
        daysOfWeek.put("MONDAY",    1);
        daysOfWeek.put("TUESDAY",   2);
        daysOfWeek.put("WEDNESDAY", 3);
        daysOfWeek.put("THURSDAY",  4);
        daysOfWeek.put("FRIDAY",    5);
        daysOfWeek.put("SATURDAY",    6);
        daysOfWeek.put("SUNDAY",    6);
        if(theDay.matches("TODAY") || theDay.matches("Today")) {
            theDay = new SimpleDateFormat("EEEE").format(new Date());
        }

        //noinspection ConstantConditions
        date = daysOfWeek.get(theDay);
    }

    Timetable(String idNumber) {
        this.idNumber = idNumber;
        daysOfWeek = new HashMap<>();
        daysOfWeek.put("MONDAY",    1);
        daysOfWeek.put("TUESDAY",   2);
        daysOfWeek.put("WEDNESDAY", 3);
        daysOfWeek.put("THURSDAY",  4);
        daysOfWeek.put("FRIDAY",    5);
    }

    @Override
    protected ArrayList<ArrayList<String[]>> doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL("http://35.189.65.75/id-timetable-v2.php/id/"+idNumber);
            urlConnection = (HttpURLConnection) url.openConnection();
            int code = urlConnection.getResponseCode();

            if(code==200){
                try( InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {

                    String line;

                    while ((line = bufferedReader.readLine()) != null)
                        result.append(line);
                }
            }
            String[] x = result.toString().split("\\{");
            List<String[]> newTimetable = new ArrayList<>();
            for (String aX : x) {
                newTimetable.add(aX.split(","));
            }

            ArrayList<String[]> dayTimetable = new ArrayList<>();
            ArrayList<ArrayList<String[]>> organizedTimetable = new ArrayList<>();
            for(int i = 0; i< newTimetable.size();i++) {
                if(Arrays.toString(newTimetable.get(i)).contains("day\":")) {
                    dayTimetable.add(newTimetable.get(i));
                }
                organizedTimetable.add(dayTimetable);
            }

            for(int i = 0; i < organizedTimetable.size(); i++)
                for(int j= 0; j < organizedTimetable.get(i).size(); j++ )
                    for(int k = 0 ; k < organizedTimetable.get(i).get(j).length; k++)
                        organizedTimetable.get(i).get(j)[k] = organizedTimetable.get(i).get(j)[k].replaceAll("\"", "");

                    return organizedTimetable;
        }
        catch (IOException e) {
            Log.e("Timetable", e.getMessage());
        }

        finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
            return new ArrayList<>();
    }
}
