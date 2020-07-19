package com.example.likemindschat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likemindschat.DataProvider;
import com.example.likemindschat.EventHandler;
import com.example.likemindschat.databinding.VhUserBinding;
import com.example.likemindschat.dtos.UserDto;
import com.example.likemindschat.viewHolders.USerVH;


public class UserAdapter extends RecyclerView.Adapter<USerVH> {

    private final DataProvider<UserDto> dataProvider;
    private final EventHandler<UserDto> eventHandler;
    private Context context;

    public UserAdapter(DataProvider<UserDto> dataProvider, EventHandler<UserDto> eventHandler, Context context) {
        this.dataProvider = dataProvider;
        this.eventHandler = eventHandler;
        this.context = context;
    }

    @NonNull
    @Override
    public USerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new USerVH(VhUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull USerVH holder, int position) {
        holder.bind(dataProvider.get(position), dataProvider.isChecked(position), eventHandler, position);
    }

    @Override
    public int getItemCount() {
        return dataProvider.getCount();
    }
}
