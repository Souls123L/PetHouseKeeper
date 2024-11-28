package com.example.pethousekeeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private List<NotesModel> notesList;

    public NotesAdapter(List<NotesModel> notesList){
        this.notesList = notesList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, description;

        public ViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle); // 修改為 tvTitle
            description = itemView.findViewById(R.id.tvDesc); // 修改為 tvDesc
        }
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position){
        NotesModel note = notesList.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
    }

    @Override
    public int getItemCount(){
        return notesList.size();
    }
}