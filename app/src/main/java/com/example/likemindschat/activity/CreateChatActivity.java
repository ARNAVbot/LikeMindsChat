package com.example.likemindschat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.likemindschat.DataProvider;
import com.example.likemindschat.EventHandler;
import com.example.likemindschat.Prefs;
import com.example.likemindschat.R;
import com.example.likemindschat.adapter.UserAdapter;
import com.example.likemindschat.databinding.ActivityCreateChatBinding;
import com.example.likemindschat.dtos.UserDto;
import com.example.likemindschat.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.likemindschat.AppConstants.CHAT_CREATOR;
import static com.example.likemindschat.AppConstants.CHAT_TITLE;
import static com.example.likemindschat.AppConstants.CHAT_WINDOW_COLLECTION;
import static com.example.likemindschat.AppConstants.CHAT_WINDOW_ID;
import static com.example.likemindschat.AppConstants.CHAT_WINDOW_IDS;
import static com.example.likemindschat.AppConstants.CREATED_TIME;
import static com.example.likemindschat.AppConstants.CREATOR_NAME;
import static com.example.likemindschat.AppConstants.ID;
import static com.example.likemindschat.AppConstants.NAME;
import static com.example.likemindschat.AppConstants.USERS_DOCUMENT_COLLECTION;
import static com.example.likemindschat.AppConstants.UUID;

public class CreateChatActivity extends AppCompatActivity implements View.OnClickListener, DataProvider<UserDto>, EventHandler<UserDto> {

    private ActivityCreateChatBinding binding;
    private FirebaseFirestore db;
    private final String TAG = "CREATE_CHAT_ACTIVITY";
    private List<UserDto> userDtoList;
    private UserAdapter userAdapter;
    private String selfUUID;
    private ProgressDialog progressDialog;
    private UserDto currentUser;
    private int selectedCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        selfUUID = Prefs.getInstance().getAuthToken();

        db = FirebaseFirestore.getInstance();
        binding.acbCreateChat.setOnClickListener(this);
        binding.actvUsersSelected.setText("Select Users (0 selected)");
        setTitle("Create Chat Group");
        setUpUSerRv();
    }

    private void setUpUSerRv() {

        userDtoList = new LinkedList<>();
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, this, this);
        binding.rvUsers.setAdapter(userAdapter);

        db.collection(USERS_DOCUMENT_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                String uuid = (String) data.get(UUID);
                                if (selfUUID.equalsIgnoreCase(uuid)) {
                                    currentUser = UserDto.Builder.userDto()
                                            .withUserName((String) data.get(NAME))
                                            .withUuid((String) data.get(UUID))
                                            .withIsChecked(false)
                                            .withChatWindowIds((List<String>) data.get(CHAT_WINDOW_IDS))
                                            .build();
                                    continue;
                                }
                                userDtoList.add(UserDto.Builder.userDto()
                                        .withUserName((String) data.get(NAME))
                                        .withUuid((String) data.get(UUID))
                                        .withIsChecked(false)
                                        .withChatWindowIds((List<String>) data.get(CHAT_WINDOW_IDS))
                                        .build());
                            }
                            userAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
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
            case R.id.acb_create_chat:
                Utils.hideKeyboard(this);
                // check if title is not empty
                if (Utils.isEmpty(binding.acetChatTitle.getEditableText().toString())) {
                    Toast.makeText(this, "Please enter title ", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check if it consists of atleast one user other than self
                Set<String> selectedUUIDs = new LinkedHashSet<>();
                for (UserDto userDto : userDtoList) {
                    if (userDto.isChecked()) {
                        selectedUUIDs.add(userDto.getUuid());
                    }
                }
                if (selectedUUIDs.size() == 0) {
                    Toast.makeText(this, "Please add atleast 1 user ", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedUUIDs.add(selfUUID);
                //make call to create chat group

                showProgressDialog();
                final String chatWindowId = Utils.getChatWindowId();

                //update chat window ids in all the users
                final List<String> chatGroupMemebersId = new LinkedList<>();
                for (UserDto userDto : userDtoList) {
                    List<String> chatWindowIds = userDto.getChatWindowIds();
                    if (chatWindowIds == null) {
                        chatWindowIds = new LinkedList<>();
                    }
                    chatWindowIds.add(chatWindowId);
                    if (userDto.isChecked()) {
                        chatGroupMemebersId.add(userDto.getUuid());
                        db.collection(USERS_DOCUMENT_COLLECTION).document(userDto.getUuid()).update(CHAT_WINDOW_IDS, chatWindowIds);
                    }
                }
                // update chat window id of current user also
                chatGroupMemebersId.add(currentUser.getUuid());
                List<String> chatWindowIds = currentUser.getChatWindowIds();
                if (chatWindowIds == null) {
                    chatWindowIds = new LinkedList<>();
                }
                chatWindowIds.add(chatWindowId);
                db.collection(USERS_DOCUMENT_COLLECTION).document(currentUser.getUuid()).update(CHAT_WINDOW_IDS, chatWindowIds);

                Map<String, Object> documentMap = new LinkedHashMap<>();
                documentMap.put(CHAT_CREATOR, Prefs.getInstance().getAuthToken());
                documentMap.put(CHAT_WINDOW_ID, chatWindowId);
                documentMap.put(CHAT_TITLE, binding.acetChatTitle.getEditableText().toString());
                documentMap.put(CREATED_TIME, System.currentTimeMillis());
                documentMap.put(CREATOR_NAME, Utils.getCreatorName());

                // now , lets create the chat window
                db.collection(CHAT_WINDOW_COLLECTION)
                        .document(chatWindowId)
                        .set(documentMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot added ");
                                WriteBatch batch = db.batch();
                                Map<String, Object> tempMap;
                                for (String uuid : chatGroupMemebersId) {
                                    tempMap = new LinkedHashMap<>();
                                    tempMap.put(ID, uuid);
                                    DocumentReference documentReference = db.collection(CHAT_WINDOW_COLLECTION)
                                            .document(chatWindowId)
                                            .collection(USERS_DOCUMENT_COLLECTION)
                                            .document(uuid);
                                    batch.set(documentReference, tempMap);
                                }
                                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // completed adding users to chat window
                                        goToChatHomeScreen();
                                        hideProgressDialog();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hideProgressDialog();
                                Log.w(TAG, "Error adding document", e);
                                showError(e.getLocalizedMessage());
                            }
                        });

                break;
        }
    }

    private void showError(String erroMessage) {
        Toast.makeText(getApplicationContext(), String.format("Failed to create group : %s", erroMessage), Toast.LENGTH_SHORT).show();
    }

    private void goToChatHomeScreen() {
        Toast.makeText(this, "Chat Group successfully created", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ChatScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public UserDto get(int position) {
        return userDtoList.get(position);
    }

    @Override
    public int getCount() {
        return userDtoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int indexOf(UserDto item) {
        return userDtoList.indexOf(item);
    }

    @Override
    public boolean contains(UserDto item) {
        return userDtoList.contains(item);
    }

    @Override
    public void remove(int position) {

    }

    @Override
    public boolean isChecked(int position) {
        UserDto userDto = userDtoList.get(position);
        if (userDto == null)
            return false;
        return userDto.isChecked();
    }

    @Override
    public void onViewClicked(View view, UserDto item, int position) {
        if (item.isChecked()) {
            selectedCount--;
        } else {
            selectedCount++;
        }
        binding.actvUsersSelected.setText(String.format("Select Users (%s selected)", selectedCount));
        item.setChecked(!item.isChecked());
        userAdapter.notifyItemChanged(position);
    }

    @Override
    public void loadMore() {

    }
}

