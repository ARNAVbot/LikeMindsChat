package com.example.likemindschat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.likemindschat.DataProvider;
import com.example.likemindschat.EventHandler;
import com.example.likemindschat.Prefs;
import com.example.likemindschat.R;
import com.example.likemindschat.adapter.ChatWindowAdapter;
import com.example.likemindschat.database.OfflineChatGroupDatabase;
import com.example.likemindschat.databinding.ActivityChatScreenBinding;
import com.example.likemindschat.dtos.ChatWindowDto;
import com.example.likemindschat.entities.OfflineChatWindowDto;
import com.example.likemindschat.viewModel.ChatGroupViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ChatScreenActivity extends AppCompatActivity implements View.OnClickListener, DataProvider<ChatWindowDto>, EventHandler<ChatWindowDto> {

    private ActivityChatScreenBinding binding;
    private List<ChatWindowDto> chatWindowDtoList;
    private ChatWindowAdapter chatWindowAdapter;
    private ChatGroupViewModel chatGroupViewModel;
    private Observer<List<OfflineChatWindowDto>> observer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatGroupViewModel = ViewModelProviders.of(this).get(ChatGroupViewModel.class);

        binding.actvWelcomeUser.setText(String.format("Welcome %s", Prefs.getInstance().getUserName()));
        binding.acbCreateChatGroup.setOnClickListener(this);
        binding.actvGroupCount.setText("You are not part of any group yet");
        binding.acbExit.setOnClickListener(this);
        setTitle("Home");
        setUpChatRv();
    }

    private void setUpChatRv() {
        chatWindowDtoList = new LinkedList<>();
        binding.rvChatWindows.setLayoutManager(new LinearLayoutManager(this));
        chatWindowAdapter = new ChatWindowAdapter(this, this);
        binding.rvChatWindows.setAdapter(chatWindowAdapter);

        fetchGroupInfo();
    }

    private void fetchGroupInfo() {
        observer = new Observer<List<OfflineChatWindowDto>>() {
            @Override
            public void onChanged(List<OfflineChatWindowDto> offlineChatWindowDtos) {
                chatWindowDtoList = new LinkedList<>();
                for (OfflineChatWindowDto offlineChatWindowDto : offlineChatWindowDtos) {
                    chatWindowDtoList.add(ChatWindowDto.Builder.chatWindowDto()
                            .withChatWindowId(offlineChatWindowDto.getChatWindowId())
                            .withCreator(offlineChatWindowDto.getCreator())
                            .withTitle(offlineChatWindowDto.getTitle())
                            .withCreatedTime(offlineChatWindowDto.getCreatedTime())
                            .withCreatorName(offlineChatWindowDto.getCreatorName())
                            .build());
                }
                Collections.reverse(chatWindowDtoList);
                if (chatWindowDtoList.size() > 0) {
                    binding.actvGroupCount.setText("You are part of the following groups.");
                } else {
                    binding.actvGroupCount.setText("You are not part of any group.");
                }
                chatWindowAdapter.notifyDataSetChanged();
            }
        };
        chatGroupViewModel.getChatGroups().observe(this, observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatGroupViewModel.getChatGroups().removeObserver(observer);
        OfflineChatGroupDatabase.destroyInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.acb_create_chat_group:
                //open create chat group activity
                intent = new Intent(this, CreateChatActivity.class);
                startActivity(intent);
                break;
            case R.id.acb_exit:
                // clear prefs and exit the app
                Prefs.getInstance().clearPrefs();
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public ChatWindowDto get(int position) {
        return chatWindowDtoList.get(position);
    }

    @Override
    public int getCount() {
        return chatWindowDtoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int indexOf(ChatWindowDto item) {
        return chatWindowDtoList.indexOf(item);
    }

    @Override
    public boolean contains(ChatWindowDto item) {
        return chatWindowDtoList.contains(item);
    }

    @Override
    public void remove(int position) {

    }

    @Override
    public boolean isChecked(int position) {
        return false;
    }

    @Override
    public void onViewClicked(View view, ChatWindowDto item, int position) {
        // open chat detail screen
        Intent intent = ChatDetailScreenActivity.newIntent(this, item.getChatWindowId(), item.getTitle());
        startActivity(intent);
    }

    @Override
    public void loadMore() {

    }
}
