package com.yuriialieksieiev.mvvmexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class ActivityAddNote extends AppCompatActivity {

    public static final String EXTRA_ID = "com.yuriialieksieiev.mvvmexample.extra.id";
    public static final String EXTRA_TITLE = "com.yuriialieksieiev.mvvmexample.extra.title";
    public static final String EXTRA_DESCRIPTION = "com.yuriialieksieiev.mvvmexample.extra.description";
    public static final String EXTRA_PRIORITY = "com.yuriialieksieiev.mvvmexample.extra.priority";

    private EditText edtTitle;
    private EditText edtDescription;
    private NumberPicker npPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        npPriority = findViewById(R.id.npPriority);
        npPriority.setMinValue(1);
        npPriority.setMaxValue(10);

        if(getIntent().hasExtra(EXTRA_ID))
        {
            setTitle("Edit Note");
            edtTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            edtDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
            npPriority.setValue(getIntent().getIntExtra(EXTRA_PRIORITY,1));
        }else
            setTitle("Add Note");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_activity_add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save_tone:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote()
    {
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        int priority = npPriority.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_TITLE,title);
        resultIntent.putExtra(EXTRA_DESCRIPTION,description);
        resultIntent.putExtra(EXTRA_PRIORITY,priority);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);

        if(id != -1)
        {
            resultIntent.setAction(MainActivity.ACTION_EDITED_NOTE);
            resultIntent.putExtra(EXTRA_ID,id);
        }else
            resultIntent.setAction(MainActivity.ACTION_ADD_NOTE);

        sendBroadcast(resultIntent);
        finish();
    }
}
