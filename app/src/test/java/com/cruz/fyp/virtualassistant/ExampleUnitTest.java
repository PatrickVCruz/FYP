package com.cruz.fyp.virtualassistant;

import android.annotation.SuppressLint;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void checkTimetable_Aho_Corasick()   {
        String[] checkWords = {"Monday", "Tuesday","Wednesday","Thursday","Friday","Today"};
        String[] words = {"timetable 15160238","timetable 15160238","timetable friday 15160238","timetable thursday 15160238"
                ,"timetable wednesday 15160238","timetable today 15160238","timetable monday 15160238"};
        Random rand = new SecureRandom();
        int number = rand.nextInt(checkWords.length);
        String day = checkWords[number];
        String speech = words[number];
        String idNumber;
        Trie trie = Trie.builder().onlyWholeWords().addKeywords(checkWords).build();

        Collection<Emit> emits = trie.parseText(day);
        emits.forEach(System.out::println);

        long runTimeAho = 0;
        long runTime0 = 0;
        Timetable timetable;

        long startTimeAho = System.nanoTime();
        for (String x : checkWords) {
            if (Arrays.toString(emits.toArray()).contains(x)) {
                idNumber = day.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
                timetable = new Timetable(idNumber, x);
                printSpecificTimetable(timetable.execute(), timetable.getDate());
                runTimeAho += System.nanoTime() - startTimeAho;
                break;
            }
            else{
                idNumber = day.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
                new Timetable(idNumber).execute();
                runTimeAho += System.nanoTime() - startTimeAho;
                break;
            }
        }

        long startTime1 = System.nanoTime();
        if(speech.contains("Today") || speech.contains("today")) {
            idNumber = speech.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
            timetable = new Timetable(idNumber, "Today");
            printSpecificTimetable(timetable.execute(), timetable.getDate());
            runTime0 += System.nanoTime() - startTime1;
        }
        else if(speech.contains("Monday") || speech.contains("monday")) {
            idNumber = speech.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
            timetable = new Timetable(idNumber, "Monday");
            printSpecificTimetable(timetable.execute(), timetable.getDate());
            runTime0 += System.nanoTime() - startTime1;
        }
        else if(speech.contains("Tuesday") || speech.contains("tuesday")) {
            idNumber = speech.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
            timetable = new Timetable(idNumber, "Tuesday");
            printSpecificTimetable(timetable.execute(), timetable.getDate());
            runTime0 += System.nanoTime() - startTime1;
        }
        else if(speech.contains("Wednesday") || speech.contains("wednesday")) {
            idNumber = speech.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
            timetable = new Timetable(idNumber, "Wednesday");
            printSpecificTimetable(timetable.execute(), timetable.getDate());
            runTime0 += System.nanoTime() - startTime1;
        }
        else if(speech.contains("Thursday") || speech.contains("thursday")) {
            idNumber = speech.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
            timetable = new Timetable(idNumber, "Thursday");
            printSpecificTimetable(timetable.execute(), timetable.getDate());
            runTime0 += System.nanoTime() - startTime1;
        }
        else if(speech.contains("Friday") || speech.contains("friday")) {
            idNumber = speech.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
            timetable = new Timetable(idNumber, "Friday");
            printSpecificTimetable(timetable.execute(), timetable.getDate());
            runTime0 += System.nanoTime() - startTime1;
        }
        else {
            idNumber = speech.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
            new Timetable(idNumber).execute();
            runTime0 += System.nanoTime() - startTime1;
        }
        System.out.println("Normal: Time in Nanoseconds " + runTime0);
        System.out.println("Normal: Time in Milliseconds " + TimeUnit.NANOSECONDS.toMillis(runTime0));

        System.out.println("Aho: Time in Nanoseconds " + runTimeAho);
        System.out.println("Aho: Time in Milliseconds " + TimeUnit.NANOSECONDS.toMillis(runTimeAho));
    }

    private void printSpecificTimetable(ArrayList<ArrayList<String[]>> organizedTimetable, int date){


        ArrayList<String> selectedDay = new ArrayList<>();
        for(int j= 0; j < organizedTimetable.get(0).size(); j++ ) {
            if( Arrays.toString(organizedTimetable.get(0).get(j)).contains("day:"+date)) {
                selectedDay.add(organizedTimetable.get(0).get(j)[3].replaceAll(".*?:", "") +"\n"
                        + organizedTimetable.get(0).get(j)[2].replaceAll(".*?:", "") +"\n"
                        + organizedTimetable.get(0).get(j)[1].substring(0,1).toUpperCase() + organizedTimetable.get(0).get(j)[1].substring(1)
                        + "\n"
                        + "Room: "+ organizedTimetable.get(0).get(j)[4].replaceAll(".*?:", ""));
            }
        }
    }

    public class Timetable {

        private String idNumber;
        private HashMap<String,Integer> daysOfWeek;
        private int date;

        int getDate() {
            return date;
        }

        @SuppressLint("SimpleDateFormat")
        Timetable(String idNumber, String today) {
            this.idNumber = idNumber;
            daysOfWeek = new HashMap<>();
            daysOfWeek.put("Monday",    1);
            daysOfWeek.put("Tuesday",   2);
            daysOfWeek.put("Wednesday", 3);
            daysOfWeek.put("Thursday",  4);
            daysOfWeek.put("Friday",    5);
            if(today.matches("today") || today.matches("Today")) {
                today = new SimpleDateFormat("EEEE").format(new Date());
            }

            //noinspection ConstantConditions
            date = daysOfWeek.get(today);
        }

        Timetable(String idNumber) {
            this.idNumber = idNumber;
            daysOfWeek = new HashMap<>();
            daysOfWeek.put("Monday",    0);
            daysOfWeek.put("Tuesday",   1);
            daysOfWeek.put("Wednesday", 2);
            daysOfWeek.put("Thursday",  3);
            daysOfWeek.put("Friday",    4);
        }

        ArrayList<ArrayList<String[]>> execute(){
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL("http://35.189.65.75/id-timetable-v2.php/id/"+idNumber);
                urlConnection = (HttpURLConnection) url.openConnection();
                int code = urlConnection.getResponseCode();

                if(code==200){
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line;

                    while ((line = bufferedReader.readLine()) != null)
                        result.append(line);
                    in.close();
                }
                String[] x = result.toString().split("\\{");
                List<String[]> newTimetable = new ArrayList<>();
                for (String aX : x) {
                    newTimetable.add(aX.split(","));
                }

                ArrayList<String[]> dayTimetable = new ArrayList<>();
                ArrayList<ArrayList<String[]>> organizedTimetable = new ArrayList<>();
                for(int i = 0; i< newTimetable.size();i++) {
                    if(Arrays.toString(newTimetable.get(i)).contains("day\":1")) {
                        dayTimetable.add(newTimetable.get(i));
                    }
                    else if(Arrays.toString(newTimetable.get(i)).contains("day\":2")) {
                        dayTimetable.add(newTimetable.get(i));
                    }
                    else if(Arrays.toString(newTimetable.get(i)).contains("day\":3")) {
                        dayTimetable.add(newTimetable.get(i));
                    }
                    else if(Arrays.toString(newTimetable.get(i)).contains("day\":4")) {
                        dayTimetable.add(newTimetable.get(i));
                    }
                    else if(Arrays.toString(newTimetable.get(i)).contains("day\":5")) {
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
                e.printStackTrace();
            }

            finally {
                assert urlConnection != null;
                urlConnection.disconnect();
            }
            return null;
        }
    }
}