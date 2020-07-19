package com.example.likemindschat.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likemindschat.DataProvider;
import com.example.likemindschat.EventHandler;
import com.example.likemindschat.dtos.ChatWindowDto;
import com.example.likemindschat.viewHolders.ChatWindowVH;

public class ChatWindowAdapter extends RecyclerView.Adapter<ChatWindowVH> {

    private final DataProvider<ChatWindowDto> dataProvider;
    private final EventHandler<ChatWindowDto> eventHandler;

    public ChatWindowAdapter(DataProvider<ChatWindowDto> dataProvider, EventHandler<ChatWindowDto> eventHandler) {
        this.dataProvider = dataProvider;
        this.eventHandler = eventHandler;
    }

    @NonNull
    @Override
    public ChatWindowVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatWindowVH(com.example.likemindschat.databinding.VhChatWindowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatWindowVH holder, int position) {
        holder.bind(dataProvider.get(position), eventHandler, position);
    }

    @Override
    public int getItemCount() {
        return dataProvider.getCount();
    }
}
