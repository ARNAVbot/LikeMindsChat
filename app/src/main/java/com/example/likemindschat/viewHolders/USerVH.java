package com.example.likemindschat.viewHolders;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.likemindschat.EventHandler;
import com.example.likemindschat.R;
import com.example.likemindschat.databinding.VhUserBinding;
import com.example.likemindschat.dtos.UserDto;

public class USerVH extends RecyclerView.ViewHolder {

    private VhUserBinding binding;
    private Context context;

    public USerVH(VhUserBinding vhUserBinding, Context context) {
        super(vhUserBinding.getRoot());
        binding = vhUserBinding;
        this.context = context;
    }

    public void bind(final UserDto userDto, boolean isChecked, final EventHandler<UserDto> eventHandler, final int position) {
        binding.actvUserName.setText(userDto.getUserName());
        if (isChecked) {
            binding.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light, null));
        } else {
            binding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.off_white, null));
        }

        binding.contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventHandler.onViewClicked(v, userDto, position);
            }
        });
    }
}
