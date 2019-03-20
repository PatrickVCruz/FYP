package com.cruz.fyp.virtualassistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cruz.fyp.virtualassistant.Azure.QnABot;
import com.cruz.fyp.virtualassistant.Database.Database;
import com.cruz.fyp.virtualassistant.GUI.messageAdapter;
import com.cruz.fyp.virtualassistant.GUI.RecyclerTouchListener;
import com.cruz.fyp.virtualassistant.Azure.Speech;
import com.cruz.fyp.virtualassistant.Azure.SpeechSynthesis.Synthesizer;
import com.cruz.fyp.virtualassistant.Azure.SpeechSynthesis.Voice;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    private Synthesizer synthesizer;
    private List<Message> messageList;
    private RecyclerView recyclerView;
    private Button speechButton;
    private Button miniSpeechButton;
    private Button keyboardButton;
    private EditText textForm;
    private ProgressBar progressBar;
    private String idNumber;
    private QnABot qnABot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recyclerView = findViewById(R.id.rv);
        speechButton = findViewById(R.id.SpeechButton);
        miniSpeechButton = findViewById(R.id.SpeechButton2);
        keyboardButton = findViewById(R.id.KeyboardButton);
        progressBar = findViewById(R.id.progressBar);
        textForm = findViewById(R.id.TextForm);

        synthesizer = new Synthesizer(getString(R.string.api_key));
        synthesizer.SetServiceStrategy(Synthesizer.ServiceStrategy.AlwaysService);
        Voice voice = new Voice("en-US", "Microsoft Server Speech Text to Speech Voice (en-US, ZiraRUS)", Voice.Gender.Male, true);
        synthesizer.SetVoice(voice, null);

        speechButton.setOnClickListener(startSpeech);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        messageList = new ArrayList<>();

        initializeData();
        initializeAdapter();

        keyboardButton.setOnClickListener(keyboardPressed);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Message message = messageList.get(position);
                Toast.makeText(getApplicationContext(), message.getMsg() + " is selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(View view, int position) {
                Message message = messageList.get(position);
//                messageList.remove(position);
//                Toast.makeText(getApplicationContext(), message.getMsg() + " is deleted!", Toast.LENGTH_SHORT).show();
//                initializeAdapter();

                Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrate.vibrate(250);
                synthesizer.SpeakToAudio(message.getMsg());
            }
        }));
    }

    private void initializeData(){
        messageList.add(new Message("Hello", Message.MessageType.TWO_ITEM));
    }

    private void initializeAdapter(){
        messageAdapter adapter = new messageAdapter(messageList);
        recyclerView.setAdapter(adapter);
    }

    public void updateAdapter(){
        recyclerView.setAdapter(new messageAdapter(messageList));
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    public void newMessage(String message) {
        messageList.add(new Message(message, Message.MessageType.TWO_ITEM));
        updateAdapter();
    }

    private void resetButtons(boolean show){
        if(show){
            speechButton.setVisibility(View.VISIBLE);
            speechButton.setClickable(true);
            progressBar.setVisibility(View.GONE);
            keyboardButton.setVisibility(View.VISIBLE);
        }
        else {
            speechButton.setVisibility(View.GONE);
            speechButton.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            keyboardButton.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener keyboardPressed = view -> {
        speechButton.setVisibility(View.GONE);
        speechButton.setClickable(false);
//        progressBar.setVisibility(View.VISIBLE);
        keyboardButton.setVisibility(View.GONE);
        textForm.setVisibility(View.VISIBLE);
        miniSpeechButton.setVisibility(View.VISIBLE);
    };

    private View.OnClickListener startSpeech = view -> {
        new collectSpeech().execute();
//        String teser = db.checkCode("asd");
//
//        newMessage(teser);
        resetButtons(false);
        Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrate.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrate.vibrate(250);
        }
    };

    @SuppressLint("StaticFieldLeak")
    public class collectSpeech extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Speech speech = new Speech();
            String captureSpeech = speech.startSpeech();
            messageList.add(new Message(captureSpeech, Message.MessageType.ONE_ITEM));
            runOnUiThread(MainActivity.this::updateAdapter);
            return captureSpeech;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.matches(".*\\d+.*") && (result.contains("Timetable") || result.contains("timetable"))) {
                Log.d("timetable", "THIS IS: " + result);
                idNumber = result.replaceAll("[a-zA-Z]","").replaceAll("[^\\d]","");
                Toast.makeText(getApplicationContext(), idNumber, Toast.LENGTH_LONG).show();
                Log.d("timetable", "THIS IS new result: " + idNumber);
                getTimeTable timeTable = new getTimeTable();
                timeTable.execute();
            }
            else if(result.contains("Where is") || result.contains("Where'S")) {
                String message = "";
                if(result.contains("Where is"))
                    message= result.replace("Where is","");
                else if(result.contains("Where'S"))
                    message = result.replace("Where'S","");
                message = message.replace(" ","").replaceAll("[^A-Za-z0-9]", "");
//                newMessage(y);
                try {
                    String z = new Think(message).execute().get();
                    String[] roomDetails = z.split(",");
                    StringBuilder details= new StringBuilder();
                    details.append(roomDetails[0]).append(" ").append(roomDetails[1]);
                    if(!String.valueOf(details).contains("Building")) {
                        if(String.valueOf(details).contains("Business"))
                            details.append(" School Building");
                        else
                            details.append(" Building");
                    }
                    newMessage(String.valueOf(details));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    qnABot = new QnABot();
                    qnABot.setQuestion(result);
                    String reply = qnABot.execute().get();
                    newMessage(reply);
                    synthesizer.SpeakToAudio(reply);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            resetButtons(true);
//            Think think = new Think(result);
//            try {
//                String x = think.execute().get();
//                newMessage(x);
//            } catch (ExecutionException | InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class getTimeTable extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
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
                return result.toString();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            finally {
                assert urlConnection != null;
                urlConnection.disconnect();
            }
            return result.toString();

        }

        @Override
        protected void onPostExecute(String timetable) {
            String[] x = timetable.split("\\{");
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


            for(int j= 0; j < organizedTimetable.get(0).size(); j++ ) {
                newMessage(organizedTimetable.get(0).get(j)[3].replaceAll(".*?:", "") +"\n"
                        + organizedTimetable.get(0).get(j)[2].replaceAll(".*?:", "") +"\n"
                        + organizedTimetable.get(0).get(j)[1].substring(0,1).toUpperCase() + organizedTimetable.get(0).get(j)[1].substring(1)+ "\n"
                        + "Room: "+ organizedTimetable.get(0).get(j)[4].replaceAll(".*?:", ""));
            }

            resetButtons(true);
        }
    }
}