package com.yuriialieksieiev.mvvmexample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_ADD_NOTE = "com.yuriialieksieiev.mvvmexample.action.addnote";

    private NoteViewModel noteViewModel;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView rcList = findViewById(R.id.rcList);
        rcList.setLayoutManager(new LinearLayoutManager(this));
        rcList.setHasFixedSize(true);
        final NoteAdapter noteAdapter = new NoteAdapter();
        rcList.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                noteAdapter.setNotes(notes);
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String title = intent.getStringExtra(ActivityAddNote.EXTRA_TITLE);
                String description = intent.getStringExtra(ActivityAddNote.EXTRA_DESCRIPTION);
                int priority = intent.getIntExtra(ActivityAddNote.EXTRA_PRIORITY,1);

                noteViewModel.insert(new Note(title,description,priority));

                Snackbar snackbar =  Snackbar.make(findViewById(R.id.main_view),"Note saved",Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(Color.rgb(0,200,0));
                snackbar.show();
            }
        };

        registerReceiver(broadcastReceiver,new IntentFilter(ACTION_ADD_NOTE));

        final FloatingActionButton addNote = findViewById(R.id.btnAddNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("com.yuriialieksieiev.mvvmexample.activity.addnote"));
            }
        });
    }

}
