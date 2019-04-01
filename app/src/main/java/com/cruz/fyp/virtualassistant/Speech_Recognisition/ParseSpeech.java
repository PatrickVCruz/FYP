package com.cruz.fyp.virtualassistant.Speech_Recognisition;

import android.util.Log;

import com.cruz.fyp.virtualassistant.Azure.QnABot;
import com.cruz.fyp.virtualassistant.GUI.Message;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class ParseSpeech {

    private static final String LOG_TAG = "ParseSpeech";
    private String speech;
    private Trie trie;

    public ParseSpeech(String speech) {
        this.speech = speech.toUpperCase();
        Log.d("Speech","Converted Speech: "+this.speech);
    }

    public Object analyseSpeech(){

        Object message = null;
        if(speech.matches(".*\\d+.*") && (speech.contains("TIMETABLE"))) {
            try {
                message = collectTimetable();
            } catch (ExecutionException | InterruptedException e) {
                Log.e(LOG_TAG, String.valueOf(e));
                Thread.currentThread().interrupt();
            }
        }
        else if(speech.contains("WHERE CAN I FIND") || speech.contains("WHERE'S") || speech.contains("WHERE IS")) {
            return findTheRoom();
        }
        else {
            try {
                QnABot qnABot = new QnABot();
                qnABot.setQuestion(speech);
                String reply = qnABot.execute().get();
                return createMessage(reply);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(LOG_TAG, String.valueOf(e));
                Thread.currentThread().interrupt();
            }
        }
        return message;
    }

    private Message createMessage(String message) {
        return new Message(message, Message.MessageType.RECEIVED);
    }

    private ArrayList<String> printSpecificTimetable(ArrayList<ArrayList<String[]>> organizedTimetable, int date){
        Log.d("Timetable","THIS IS printSpecificTimetable " + date);
        ArrayList<String> selectedDay = new ArrayList<>();
        if(date == 6) {
            selectedDay.add("There are no classes on the weekend");
        }
        else {
            for (int j = 0; j < organizedTimetable.get(0).size(); j++) {
                if (Arrays.toString(organizedTimetable.get(0).get(j)).contains("day:" + date)) {
                    selectedDay.add(organizedTimetable.get(0).get(j)[3].replaceAll(".*?:", "") + "\n"
                            + organizedTimetable.get(0).get(j)[2].replaceAll(".*?:", "") + "\n"
                            + organizedTimetable.get(0).get(j)[1].substring(0, 1).toUpperCase() + organizedTimetable.get(0).get(j)[1].substring(1) + "\n"
                            + "Room: " + organizedTimetable.get(0).get(j)[4].replaceAll(".*?:", ""));
                    Log.d("timetable", "THIS IS " + Arrays.toString(organizedTimetable.get(0).get(j)));
                }
            }
        }
        return selectedDay;
    }

    private Object collectTimetable() throws ExecutionException, InterruptedException {
        String[] checkWords = {"MONDAY", "TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","TODAY","SATURDAY","SUNDAY"};
        trie = Trie.builder().onlyWholeWords().addKeywords(checkWords).build();
        String idNumber;
        Collection<Emit> emits = trie.parseText(speech);

        for (String word : checkWords) {
            Log.d(LOG_TAG, "Word now: " + word);
            if (Arrays.toString(emits.toArray()).contains(word)) {
                idNumber = speech.replaceAll("[a-zA-Z]", "").replaceAll("[^\\d]", "");
                Timetable timetable = new Timetable(idNumber, word);
                Log.d(LOG_TAG, "Word found: " + word);

                return printSpecificTimetable(timetable.execute().get(), timetable.getDate());
            }
        }
        idNumber = speech.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
        return new Timetable(idNumber).execute().get();
    }

    private Message findTheRoom() {
        String[] keyWords = {"WHERE IS", "WHERE'S", "WHERE CAN I FIND", "WHERE IS THE", "WHERE'S THE", "WHERE CAN I FIND THE"};
        trie = Trie.builder().onlyWholeWords().addKeywords(keyWords).build();
        Collection<Emit> emits = trie.parseText(speech);
        String roomCode = null;
        for(String word: keyWords) {
            if(Arrays.toString(emits.toArray()).contains(word)) {
                roomCode = speech.replaceAll(word,"");
            }
        }
        assert roomCode != null;
        int roomName;
        roomCode = roomCode.replaceAll("\\?","");
        if(roomCode.contains(".*\\d.*")) {
            roomCode = roomCode.replaceAll(" ", "").replaceAll("[^A-Za-z0-9]", "");
            roomName = 0;
        }
        else{
            roomCode = roomCode.replaceFirst(" ","").replaceAll(" ", "%20");
            roomName = 1;
        }
            try {
                String room = new FindTheRoom(roomCode,roomName).execute().get();
                Log.d("expected", room);
                if (room.equals("")) {
                    return createMessage("Not a room code");
                }
                String[] roomDetails = room.split(";");
                String details = roomDetails[0] + "\n" + roomDetails[1] +
                        " Building" + "\n" + roomDetails[2];
                return createMessage(String.valueOf(details));
            } catch (ExecutionException | InterruptedException e) {
                Log.e(LOG_TAG, String.valueOf(e));
                Thread.currentThread().interrupt();
            }

        return createMessage("I'm sorry I don't know where that is?");
    }

}
