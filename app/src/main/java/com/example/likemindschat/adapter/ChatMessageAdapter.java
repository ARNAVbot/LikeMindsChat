package com.example.likemindschat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likemindschat.DataProvider;
import com.example.likemindschat.databinding.VhChatMessageBinding;
import com.example.likemindschat.dtos.ChatDto;
import com.example.likemindschat.viewHolders.ChatMessageVH;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageVH> {

    private DataProvider<ChatDto> dataProvider;
    private Context context;

    public ChatMessageAdapter(DataProvider<ChatDto> dataProvider, Context context) {
        this.dataProvider = dataProvider;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatMessageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatMessageVH(VhChatMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageVH holder, int position) {
        holder.bind(dataProvider.get(position), context);
    }

    @Override
    public int getItemCount() {
        return dataProvider.getCount();
    }
}
