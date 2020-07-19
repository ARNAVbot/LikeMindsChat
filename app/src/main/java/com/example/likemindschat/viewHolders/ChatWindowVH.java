package com.example.likemindschat.viewHolders;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.likemindschat.EventHandler;
import com.example.likemindschat.databinding.VhChatWindowBinding;
import com.example.likemindschat.dtos.ChatWindowDto;

public class ChatWindowVH extends RecyclerView.ViewHolder {

    private VhChatWindowBinding binding;

    public ChatWindowVH(VhChatWindowBinding vhChatWindowBinding) {
        super(vhChatWindowBinding.getRoot());
        binding = vhChatWindowBinding;
    }

    public void bind(final ChatWindowDto chatWindowDto, final EventHandler<ChatWindowDto> eventHandler, final int position) {
        binding.actvGroupName.setText(chatWindowDto.getTitle());
        binding.actvCreatorName.setText("Creator: ".concat(chatWindowDto.getCreatorName()));
        binding.contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventHandler.onViewClicked(v, chatWindowDto, position);
            }
        });
    }
}
