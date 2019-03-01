package com.yuriialieksieiev.mvvmexample;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

 class NoteRepository
{
    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;

     NoteRepository(Application application)
    {
        NoteDB noteDB = NoteDB.getInstance(application);
        noteDAO = noteDB.noteDAO();
        allNotes = noteDAO.getAllNotes();
    }

     void insert(Note note)
    {
        new InsertAsyncTask(noteDAO).execute(note);
    }

     void update(Note note)
    {
        new UpDateAsyncTask(noteDAO).execute(note);
    }

     void delete(Note note)
    {
        new DeleteAsyncTask(noteDAO).execute(note);
    }

     void deleteAll()
    {
        new DeleteAllAsyncTask(noteDAO).execute();
    }

     LiveData<List<Note>> getAllNotes()
    {
        return allNotes;
    }

    private static class InsertAsyncTask extends AsyncTask<Note,Void,Void>
    {
        private NoteDAO noteDAO;

        private InsertAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.insert(notes[0]);
            return null;
        }
    }

    private static class UpDateAsyncTask extends AsyncTask<Note,Void,Void>
    {
        private NoteDAO noteDAO;

        private UpDateAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.upDate(notes[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Note,Void,Void>
    {
        private NoteDAO noteDAO;

        private DeleteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private NoteDAO noteDAO;

        private DeleteAllAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Void... notes) {
            noteDAO.deleteAllNotes();
            return null;
        }
    }

}
