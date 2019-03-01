package com.yuriialieksieiev.mvvmexample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>
{

    public interface OnNoteClickListener{
        void onClick(Note note);
    }

    private List<Note> notes = new ArrayList<>();
    private OnNoteClickListener onNoteClickListener;

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        final View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_note,viewGroup,false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        final Note currentNote = notes.get(i);
        noteViewHolder.tvTitle.setText(currentNote.getTitle());
        noteViewHolder.tvDescription.setText(currentNote.getDescription());
        noteViewHolder.tvPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    void setNotes(List<Note> notes)
    {
        this.notes = notes;
        notifyDataSetChanged();
    }

    Note getNoteAt(int i)
    {
        return notes.get(i);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvPriority;

        private NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPriority = itemView.findViewById(R.id.tvPriority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onNoteClickListener != null)
                        onNoteClickListener.onClick(getNoteAt(getAdapterPosition()));
                }
            });
        }
    }
}
