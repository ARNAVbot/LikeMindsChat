package com.example.likemindschat.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.likemindschat.DaoThreads.OfflineChatGroupStoreThreads;
import com.example.likemindschat.database.OfflineChatDatabaseInitializer;
import com.example.likemindschat.database.OfflineChatGroupDatabase;
import com.example.likemindschat.dtos.ChatDto;
import com.example.likemindschat.entities.OfflineChatDto;
import com.example.likemindschat.entities.OfflineChatWindowDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.likemindschat.App.getAppContext;
import static com.example.likemindschat.AppConstants.BODY;
import static com.example.likemindschat.AppConstants.CHAT_CREATOR;
import static com.example.likemindschat.AppConstants.CHAT_TITLE;
import static com.example.likemindschat.AppConstants.CHAT_WINDOW_COLLECTION;
import static com.example.likemindschat.AppConstants.CHAT_WINDOW_ID;
import static com.example.likemindschat.AppConstants.CREATED_TIME;
import static com.example.likemindschat.AppConstants.CREATOR_NAME;
import static com.example.likemindschat.AppConstants.MESSAGE_ID;

public class ChatGroupViewModel extends AndroidViewModel {

    private FirebaseFirestore db;
    private LiveData<List<OfflineChatWindowDto>> offlineChatWindowDtoList;

    private final String TAG = "CHAT_GROUP_VIEW_MODEL";

    public ChatGroupViewModel(@NonNull Application application) {
        super(application);
        db = FirebaseFirestore.getInstance();
    }

    private void getChatGroupsFromFirebase() {
        db.collection(CHAT_WINDOW_COLLECTION)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }
                        List<DocumentChange> documentChanges = value.getDocumentChanges();
                        if (documentChanges.size() != 0) {
                            OfflineChatWindowDto[] offlineChatWindowDtos = new OfflineChatWindowDto[documentChanges.size()];
                            int i = 0;
                            for (DocumentChange documentChange : documentChanges) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    Map<String, Object> data = documentChange.getDocument().getData();
                                    offlineChatWindowDtos[i] = OfflineChatWindowDto.Builder.offlineChatWindowDto()
                                            .withChatWindowId((String) data.get(CHAT_WINDOW_ID))
                                            .withCreator((String) data.get(CHAT_CREATOR))
                                            .withTitle((String) data.get(CHAT_TITLE))
                                            .withCreatedTime((Long) data.get(CREATED_TIME))
                                            .withCreatorName((String) data.get(CREATOR_NAME))
                                            .build();
                                    i++;
                                }
                            }
                            // todo: handle cases where the document is MODIFIED / REMOVED
                            Thread offlineChatGroupStoreThread = new Thread(new OfflineChatGroupStoreThreads(offlineChatWindowDtos));
                            offlineChatGroupStoreThread.start();
                        }
                    }
                });
    }

    public LiveData<List<OfflineChatWindowDto>> getChatGroups() {
        offlineChatWindowDtoList = OfflineChatDatabaseInitializer.fetchAllChatGroups(OfflineChatGroupDatabase.getAppDatabase(
                getAppContext()));
        getChatGroupsFromFirebase();
        return offlineChatWindowDtoList;
    }
}
