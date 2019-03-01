package com.yuriialieksieiev.mvvmexample;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note note, @NonNull Note t1) {
            return note.getId() == t1.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note note, @NonNull Note t1) {
            return note.getTitle().equals(t1.getTitle())
                    && note.getDescription().equals(t1.getDescription())
                    && note.getPriority() == t1.getPriority();
        }
    };

    public interface OnNoteClickListener {
        void onClick(Note note);
    }

    private OnNoteClickListener onNoteClickListener;

    void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_note, viewGroup, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        final Note currentNote = getItem(i);
        noteViewHolder.tvTitle.setText(currentNote.getTitle());
        noteViewHolder.tvDescription.setText(currentNote.getDescription());
        noteViewHolder.tvPriority.setText(String.valueOf(currentNote.getPriority()));
    }




    Note getNoteAt(int i) {
        return getItem(i);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
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
                    if (onNoteClickListener != null)
                        onNoteClickListener.onClick(getNoteAt(getAdapterPosition()));
                }
            });
        }
    }
}
