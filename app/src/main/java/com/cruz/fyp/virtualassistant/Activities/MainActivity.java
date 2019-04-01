package com.cruz.fyp.virtualassistant.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cruz.fyp.virtualassistant.Azure.Speech;
import com.cruz.fyp.virtualassistant.Azure.SpeechSynthesis.Synthesizer;
import com.cruz.fyp.virtualassistant.Azure.SpeechSynthesis.Voice;
import com.cruz.fyp.virtualassistant.GUI.Message;
import com.cruz.fyp.virtualassistant.GUI.RecyclerTouchListener;
import com.cruz.fyp.virtualassistant.GUI.MessageAdapter;
import com.cruz.fyp.virtualassistant.R;
import com.cruz.fyp.virtualassistant.Speech_Recognisition.ParseSpeech;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Synthesizer synthesizer;
    private List<Message> messageList;
    private RecyclerView recyclerView;
    private EditText textForm;
    private ProgressBar progressBar;
    private ConstraintLayout keyboardLayout;
    private ConstraintLayout mainButtonLayout;
    private Vibrator vibrate;
    private ParseSpeech questionAsked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recyclerView = findViewById(R.id.Chat_Window);
        progressBar = findViewById(R.id.progressBar);
        textForm = findViewById(R.id.TextForm);
        keyboardLayout = findViewById(R.id.TextBoxContainer);
        mainButtonLayout = findViewById(R.id.MainButtonLayout);

        Button speechButton = findViewById(R.id.SpeechButton);
        Button miniMicrophone = findViewById(R.id.SpeechButton2);
        Button keyboardButton = findViewById(R.id.KeyboardButton);
        Button sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(textEntered);

        synthesizer = new Synthesizer(getString(R.string.api_key));
        synthesizer.setServiceStrategy(Synthesizer.ServiceStrategy.ALWAYS_SERVICE);
        Voice voice = new Voice("en-US", "Microsoft Server Speech Text to Speech Voice (en-US, ZiraRUS)", Voice.Gender.MALE);
        synthesizer.setVoice(voice);

        speechButton.setOnClickListener(startSpeech);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        messageList = new ArrayList<>();

        messageList.add(new Message("How can I help you today?", Message.MessageType.RECEIVED));
        initializeAdapter();
        synthesizer.speakToAudio("How can I help you today?");

        keyboardButton.setOnClickListener(keyboardPressed);
        miniMicrophone.setOnClickListener(showMicrophone);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Message message = messageList.get(position);
                Toast.makeText(getApplicationContext(), message.getMsg() + " is selected!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLongClick(View view, int position) {
                Message message = messageList.get(position);
                if(synthesizer.getTalking()) {
                    synthesizer.stopSound();
                }
                else {
                    vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrate.vibrate(250);
                    synthesizer.speakToAudio(message.getMsg());
                }
            }
        }));
    }

    private void initializeAdapter(){
        MessageAdapter adapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(adapter);
    }

    public void updateAdapter(){
        recyclerView.setAdapter(new MessageAdapter(messageList));
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    public void createMessage(String message) {
        messageList.add(new Message(message, Message.MessageType.RECEIVED));
        updateAdapter();
    }

    private void resetButtons(boolean show){
        if(show){
            mainButtonLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        else {
            mainButtonLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener showMicrophone = view-> {
        mainButtonLayout.setVisibility(View.VISIBLE);
        keyboardLayout.setVisibility(View.GONE);
    };

    private View.OnClickListener keyboardPressed = view -> {
        mainButtonLayout.setVisibility(View.GONE);
        keyboardLayout.setVisibility(View.VISIBLE);
    };

    private View.OnClickListener startSpeech = view -> {
        new CollectSpeech().execute();
        resetButtons(false);
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrate.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrate.vibrate(250);
        }
    };

    private View.OnClickListener textEntered = view -> {
        String question = textForm.getText().toString();
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrate.vibrate(250);
        if(question.matches("")) {
            Toast.makeText(this, "Please enter a question", Toast.LENGTH_SHORT).show();
        }
        else{
            messageList.add(new Message(question, Message.MessageType.SENT));
            updateAdapter();
            textForm.setText("");
            questionAsked = new ParseSpeech(question);
            Object obj = questionAsked.analyseSpeech();
              if(obj instanceof Message) {
                  messageList.add((Message) obj);
                  synthesizer.speakToAudio(((Message) obj).getMsg());
                }
            else if(obj instanceof ArrayList) {
              if(((ArrayList) obj).get(0) instanceof String){
                  printSelectedTimetable((ArrayList<String>) obj);
              }
              else{
                  printWholeTimetable((ArrayList<ArrayList<String[]>>)  obj);
              }
          }
      }
      updateAdapter();
    };

    @SuppressLint("StaticFieldLeak")
    public class CollectSpeech extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Speech speech = new Speech();
            String captureSpeech = speech.startSpeech();
            messageList.add(new Message(captureSpeech, Message.MessageType.SENT));
            runOnUiThread(MainActivity.this::updateAdapter);
            return captureSpeech;
        }

        @Override
        protected void onPostExecute(String result) {
            questionAsked = new ParseSpeech(result);
            Object obj = questionAsked.analyseSpeech();
            if(obj instanceof Message) {
                messageList.add((Message) obj);
                synthesizer.speakToAudio(((Message) obj).getMsg());
            }
            else if(obj instanceof ArrayList) {
                if(((ArrayList) obj).get(0) instanceof String){
                    printSelectedTimetable((ArrayList<String>) obj);
                }
                else{
                    printWholeTimetable((ArrayList<ArrayList<String[]>>)  obj);
                }
            }
            resetButtons(true);
        }
    }

    private void printWholeTimetable(ArrayList<ArrayList<String[]>> organizedTimetable){
        for(int j= 0; j < organizedTimetable.get(0).size(); j++ ) {
            createMessage(organizedTimetable.get(0).get(j)[3].replaceAll(".*?:", "") +"\n"
                    + organizedTimetable.get(0).get(j)[2].replaceAll(".*?:", "") +"\n"
                    + organizedTimetable.get(0).get(j)[1].substring(0,1).toUpperCase() + organizedTimetable.get(0).get(j)[1].substring(1)+ "\n"
                    + "Room: "+ organizedTimetable.get(0).get(j)[4].replaceAll(".*?:", ""));
        }
    }

    private void printSelectedTimetable(ArrayList<String> selectedDay) {
        for(int i = 0; i < selectedDay.size(); i++) {
            createMessage(selectedDay.get(i));
        }
    }
}