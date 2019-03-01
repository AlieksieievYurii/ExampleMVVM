package com.yuriialieksieiev.mvvmexample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_ADD_NOTE = "com.yuriialieksieiev.mvvmexample.action.addnote";
    public static final String ACTION_EDITED_NOTE = "com.yuriialieksieiev.mvvmexample.action.editednote";

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
                noteAdapter.submitList(notes);
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getStringExtra(ActivityAddNote.EXTRA_TITLE);
                String description = intent.getStringExtra(ActivityAddNote.EXTRA_DESCRIPTION);
                int priority = intent.getIntExtra(ActivityAddNote.EXTRA_PRIORITY, 1);

                if(Objects.equals(intent.getAction(), ACTION_EDITED_NOTE))
                {
                    int id = intent.getIntExtra(ActivityAddNote.EXTRA_ID,-1);
                    Note note = new Note(title,description,priority);
                    note.setId(id);
                    noteViewModel.upDate(note);

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.main_view), "Note edited", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(Color.rgb(0, 200, 0));
                    snackbar.show();

                }else if(Objects.equals(intent.getAction(),ACTION_ADD_NOTE)) {

                    noteViewModel.insert(new Note(title, description, priority));

                    Snackbar snackbar = Snackbar.make(findViewById(R.id.main_view), "Note added", Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(Color.rgb(0, 200, 0));
                    snackbar.show();
                }
            }
        };

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ADD_NOTE);
        intentFilter.addAction(ACTION_EDITED_NOTE);
        registerReceiver(broadcastReceiver, intentFilter);

        final FloatingActionButton addNote = findViewById(R.id.btnAddNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("com.yuriialieksieiev.mvvmexample.activity.addeditnote"));
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));

                Snackbar snackbar = Snackbar.make(findViewById(R.id.main_view), "Note removed", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(Color.rgb(200, 0, 0));
                snackbar.show();
            }
        }).attachToRecyclerView(rcList);

        noteAdapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onClick(Note note) {
                Intent intent = new Intent("com.yuriialieksieiev.mvvmexample.activity.addeditnote");
                intent.putExtra(ActivityAddNote.EXTRA_ID,note.getId());
                intent.putExtra(ActivityAddNote.EXTRA_TITLE,note.getTitle());
                intent.putExtra(ActivityAddNote.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(ActivityAddNote.EXTRA_PRIORITY,note.getPriority());

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_remove_all:
                removeAllNotes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeAllNotes() {
        noteViewModel.deleteAllNotes();

        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_view), "All notes removed", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(Color.rgb(200, 0, 0));
        snackbar.show();
    }
}
