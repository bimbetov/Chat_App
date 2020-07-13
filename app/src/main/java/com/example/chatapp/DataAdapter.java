package com.example.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private List<Room> rooms;
    private OnChatListener mOnChatListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        OnChatListener onChatListener;

        MyViewHolder(View v, OnChatListener onChatListener) {
            super(v);
            textView = v.findViewById(R.id.chatName);
            this.onChatListener = onChatListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onChatListener.onChatClick(getAdapterPosition());
        }
    }

    public DataAdapter(List<Room> rooms, OnChatListener onChatListener) {
        this.rooms = rooms;
        this.mOnChatListener = onChatListener;
    }

    @NonNull
    @Override
    public DataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room, parent, false);
        return new MyViewHolder(view, mOnChatListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.textView.setText(room.getChatName());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public interface OnChatListener {
        void onChatClick(int position);
    }
}
