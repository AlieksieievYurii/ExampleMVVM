package com.yuriialieksieiev.mvvmexample;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Note.class}, version = 1)
abstract class NoteDB extends RoomDatabase
{
    private static NoteDB instance;

    abstract NoteDAO noteDAO();

    static synchronized NoteDB getInstance(Context context)
    {
        if(instance == null)
            instance =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            NoteDB.class,
                            "note_database")
                            .fallbackToDestructiveMigration()
                            .build();

        return instance;
    }
}
