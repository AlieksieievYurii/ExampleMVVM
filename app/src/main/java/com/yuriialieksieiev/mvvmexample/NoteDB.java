package com.yuriialieksieiev.mvvmexample;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Note.class}, version = 1)
abstract class NoteDB extends RoomDatabase
{
    private static final String NAME_DB = "note_database";

    private static NoteDB instance;

    abstract NoteDAO noteDAO();

    static synchronized NoteDB getInstance(Context context)
    {
        if(instance == null)
            instance =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            NoteDB.class,
                            NAME_DB)
                            .fallbackToDestructiveMigration()
                            .addCallback(callback)
                            .build();

        return instance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private NoteDAO noteDAO;

        private PopulateDbAsyncTask(NoteDB noteDB)
        {
            this.noteDAO = noteDB.noteDAO();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            noteDAO.insert(new Note("Note 1","Description 1",1));
            noteDAO.insert(new Note("Note 2","Description 2",2));
            noteDAO.insert(new Note("Note 3","Description 3",3));
            noteDAO.insert(new Note("Note 4","Description 4",4));
            return null;
        }
    }
}
