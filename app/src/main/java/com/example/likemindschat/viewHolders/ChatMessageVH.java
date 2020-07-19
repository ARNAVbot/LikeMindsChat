package com.example.likemindschat.viewHolders;

import android.content.Context;
import android.view.Gravity;

import androidx.recyclerview.widget.RecyclerView;

import com.example.likemindschat.Prefs;
import com.example.likemindschat.databinding.VhChatMessageBinding;
import com.example.likemindschat.dtos.ChatDto;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChatMessageVH extends RecyclerView.ViewHolder {

    private VhChatMessageBinding binding;

    public ChatMessageVH(VhChatMessageBinding vhChatMessageBinding) {
        super(vhChatMessageBinding.getRoot());
        binding = vhChatMessageBinding;
    }

    public void bind(ChatDto chatDto, Context context) {
        binding.actvMessage.setText(chatDto.getBody());
        binding.actvCreatorName.setText(chatDto.getCreatorName());
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        binding.actvDate.setText(simpleDateFormat.format(chatDto.getDateCreated()));

        if (chatDto.getCreator().equalsIgnoreCase(Prefs.getInstance().getAuthToken())) {
            binding.actvCreatorName.setGravity(Gravity.END);
            binding.actvMessage.setGravity(Gravity.END);
            binding.actvDate.setGravity(Gravity.END);
            binding.actvCreatorName.setTextColor(context.getResources().getColor(android.R.color.black, null));
        } else {
            binding.actvCreatorName.setGravity(Gravity.START);
            binding.actvMessage.setGravity(Gravity.START);
            binding.actvDate.setGravity(Gravity.START);
            binding.actvCreatorName.setTextColor(context.getResources().getColor(android.R.color.holo_red_light, null));
        }
    }
}
