package com.cruz.fyp.virtualassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private List<Person> personList;
    private RecyclerView recyclerView;
    private Button speechButton;
    private MyAdapter adapter;
    private ProgressBar progressBar;
    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv);
        speechButton = findViewById(R.id.SpeechButton);

        speechButton.setOnClickListener(startSpeech);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        personList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);

        initializeData();
        initializeAdapter();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Person movie = personList.get(position);
                Toast.makeText(getApplicationContext(), movie.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Person movie = personList.get(position);
                personList.remove(position);
                Toast.makeText(getApplicationContext(), movie.getName() + " is deleted!", Toast.LENGTH_SHORT).show();
                initializeAdapter();
            }
        }));

    }



    private void initializeData(){
        personList.add(new Person("Hello", Person.PersonType.TWO_ITEM));
    }

    private void initializeAdapter(){
        adapter = new MyAdapter(personList);
        recyclerView.setAdapter(adapter);
    }

    private View.OnClickListener startSpeech = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            personList.add(new Person("What's up", Person.PersonType.ONE_ITEM));
            recyclerView.setAdapter(new MyAdapter(personList));

            speechButton.setClickable(false);
//            progressBar.setVisibility(View.VISIBLE);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
//                    progressBar.setVisibility(View.INVISIBLE);

                    speechButton.setClickable(true);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            personList.add(new Person("Nothing", Person.PersonType.TWO_ITEM));
                            personList.add(new Person("@drawable/ghost", Person.PersonType.TWO_ITEM));
                            recyclerView.setAdapter(new MyAdapter(personList));
                            recyclerView.scrollToPosition(personList.size() - 1);
                        }
                    });
                }
            },5000);
        }
    };

}