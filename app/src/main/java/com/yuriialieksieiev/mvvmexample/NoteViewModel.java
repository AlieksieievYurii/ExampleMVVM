package com.yuriialieksieiev.mvvmexample;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

     void insert(Note note)
    {
        repository.insert(note);
    }

     void upDate(Note note)
    {
        repository.update(note);
    }

     void delete(Note note)
    {
        repository.delete(note);
    }

     void deleteAllNotes()
    {
        repository.deleteAll();
    }

     LiveData<List<Note>> getAllNotes()
    {
        return allNotes;
    }
}
