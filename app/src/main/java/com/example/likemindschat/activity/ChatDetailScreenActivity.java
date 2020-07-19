package com.example.likemindschat.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.likemindschat.DataProvider;
import com.example.likemindschat.Prefs;
import com.example.likemindschat.R;
import com.example.likemindschat.adapter.ChatMessageAdapter;
import com.example.likemindschat.database.OfflineChatMessageDatabase;
import com.example.likemindschat.databinding.ActivityChatDetailBinding;
import com.example.likemindschat.dtos.ChatDto;
import com.example.likemindschat.entities.OfflineChatDto;
import com.example.likemindschat.utils.Utils;
import com.example.likemindschat.viewModel.ChatMessageViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.likemindschat.AppConstants.BODY;
import static com.example.likemindschat.AppConstants.CHAT_CREATOR;
import static com.example.likemindschat.AppConstants.CHAT_TITLE;
import static com.example.likemindschat.AppConstants.CHAT_WINDOW_COLLECTION;
import static com.example.likemindschat.AppConstants.CHAT_WINDOW_ID;
import static com.example.likemindschat.AppConstants.CREATED_TIME;
import static com.example.likemindschat.AppConstants.CREATOR_NAME;
import static com.example.likemindschat.AppConstants.MESSAGES_COLLECTION;
import static com.example.likemindschat.AppConstants.MESSAGE_ID;

public class ChatDetailScreenActivity extends AppCompatActivity implements View.OnClickListener, DataProvider<ChatDto> {

    private ActivityChatDetailBinding binding;
    private final String TAG = "CHAT_DETAIL_SCREEN";
    private String chatWindowId;
    private String chatTitel;
    private List<ChatDto> chatDtoList;
    private ChatMessageAdapter chatMessageAdapter;
    private ProgressDialog progressDialog;
    private ChatMessageViewModel chatMessageViewModel;
    private Observer<List<OfflineChatDto>> observer;
    private FirebaseFirestore db;

    public static Intent newIntent(Context context, String chatWindowId, String chatGroupName) {
        Intent intent = new Intent(context, ChatDetailScreenActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHAT_WINDOW_ID, chatWindowId);
        bundle.putSerializable(CHAT_TITLE, chatGroupName);
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chatWindowId = extras.getString(CHAT_WINDOW_ID);
            chatTitel = extras.getString(CHAT_TITLE);
        }

        chatMessageViewModel = ViewModelProviders.of(this).get(ChatMessageViewModel.class);

        binding.acbSend.setOnClickListener(this);
        setTitle(chatTitel);
        setUpChatsRv();
    }

    private void setUpChatsRv() {
        chatDtoList = new LinkedList<>();
        binding.rvChats.setLayoutManager(new LinearLayoutManager(this));
        chatMessageAdapter = new ChatMessageAdapter(this, this);
        binding.rvChats.setAdapter(chatMessageAdapter);

        observer = new Observer<List<OfflineChatDto>>() {
            @Override
            public void onChanged(List<OfflineChatDto> offlineChatDtos) {
                chatDtoList = new LinkedList<>();
                for (OfflineChatDto offlineChatDto : offlineChatDtos) {
                    chatDtoList.add(ChatDto.Builder
                            .chatDto()
                            .withBody(offlineChatDto.getBody())
                            .withCreatorName(offlineChatDto.getCreatorName())
                            .withCreator(offlineChatDto.getCreator())
                            .withDateCreated(offlineChatDto.getDateCreated())
                            .withMessageId(offlineChatDto.getMessageId())
                            .withChatWindowId(offlineChatDto.getChatWindowId())
                            .build());
                }
                chatMessageAdapter.notifyDataSetChanged();
            }
        };
        chatMessageViewModel.getChatMessages(chatWindowId).observe(this, observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatMessageViewModel.getChatMessages(chatWindowId).removeObserver(observer);
        OfflineChatMessageDatabase.destroyInstance();
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if ((progressDialog != null) && (progressDialog.isShowing())) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acb_send:
                // send message to the document location
                String messageToSend = binding.acetMessage.getEditableText().toString();
                if (Utils.isEmpty(messageToSend)) {
                    return;
                }
                showProgressDialog();
                String messageId = Utils.getChatWindowId();
                Map<String, Object> documentMap = new LinkedHashMap<>();
                documentMap.put(CHAT_CREATOR, Prefs.getInstance().getAuthToken());
                documentMap.put(CREATED_TIME, System.currentTimeMillis());
                documentMap.put(CREATOR_NAME, Utils.getCreatorName());
                documentMap.put(MESSAGE_ID, messageId);
                documentMap.put(BODY, messageToSend);
                documentMap.put(CHAT_WINDOW_ID, chatWindowId);

                // now , lets create the chat window
                db.collection(CHAT_WINDOW_COLLECTION)
                        .document(chatWindowId)
                        .collection(MESSAGES_COLLECTION)
                        .document(messageId)
                        .set(documentMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot added ");
                                hideProgressDialog();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                hideProgressDialog();
                                showError(e.getLocalizedMessage());
                            }
                        });

                binding.acetMessage.setText("");
                break;
        }
    }

    private void showError(String errorMEssage) {
        Toast.makeText(this, String.format("Failed to send message : %s", errorMEssage), Toast.LENGTH_SHORT).show();
    }

    @Override
    public ChatDto get(int position) {
        return chatDtoList.get(position);
    }

    @Override
    public int getCount() {
        return chatDtoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int indexOf(ChatDto item) {
        return chatDtoList.indexOf(item);
    }

    @Override
    public boolean contains(ChatDto item) {
        return chatDtoList.contains(item);
    }

    @Override
    public void remove(int position) {

    }

    @Override
    public boolean isChecked(int position) {
        return false;
    }
}
