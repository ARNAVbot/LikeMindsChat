package com.example.likemindschat.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.likemindschat.DaoThreads.OfflineChatStoreThread;
import com.example.likemindschat.database.OfflineChatDatabaseInitializer;
import com.example.likemindschat.database.OfflineChatMessageDatabase;
import com.example.likemindschat.dtos.ChatDto;
import com.example.likemindschat.entities.OfflineChatDto;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.likemindschat.App.getAppContext;
import static com.example.likemindschat.AppConstants.BODY;
import static com.example.likemindschat.AppConstants.CHAT_CREATOR;
import static com.example.likemindschat.AppConstants.CHAT_WINDOW_COLLECTION;
import static com.example.likemindschat.AppConstants.CHAT_WINDOW_ID;
import static com.example.likemindschat.AppConstants.CREATED_TIME;
import static com.example.likemindschat.AppConstants.CREATOR_NAME;
import static com.example.likemindschat.AppConstants.MESSAGES_COLLECTION;
import static com.example.likemindschat.AppConstants.MESSAGE_ID;


public class ChatMessageViewModel extends AndroidViewModel {

    private final String TAG = "CHAT_VIEW_MODEL";

    private LiveData<List<OfflineChatDto>> offlineChatDtoLiveData;
    private ListenerRegistration listenerRegistration;
    private FirebaseFirestore db;

    public ChatMessageViewModel(@NonNull Application application) {
        super(application);
        db = FirebaseFirestore.getInstance();
    }

    private void getChatMessagesFromFirebase(String chatWindowId) {
        final CollectionReference collRef = db.collection(CHAT_WINDOW_COLLECTION).document(chatWindowId).collection(MESSAGES_COLLECTION);
        listenerRegistration = collRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                if (documentChanges.size() != 0) {
                    List<ChatDto> chatDtosToSync = new LinkedList<>();
                    for (DocumentChange documentChange : documentChanges) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            Map<String, Object> data = documentChange.getDocument().getData();
                            ChatDto chatDto = ChatDto.Builder.chatDto()
                                    .withBody((String) data.get(BODY))
                                    .withCreatorName((String) data.get(CREATOR_NAME))
                                    .withCreator((String) data.get(CHAT_CREATOR))
                                    .withDateCreated((long) data.get(CREATED_TIME))
                                    .withMessageId((String) data.get(MESSAGE_ID))
                                    .withChatWindowId((String) data.get(CHAT_WINDOW_ID))
                                    .build();
                            chatDtosToSync.add(chatDto);
                        }
                        // todo: handle cases where the document is MODIFIED / REMOVED
                    }
                    addDataToOfflineStorage(chatDtosToSync);
                }
            }
        });
    }

    private void addDataToOfflineStorage(List<ChatDto> chatDtoList) {
        OfflineChatDto[] offlineChatDtoList = new OfflineChatDto[chatDtoList.size()];
        int i = 0;
        for (ChatDto chatDto : chatDtoList) {
            offlineChatDtoList[i] = OfflineChatDto.Builder.offlineChatDto()
                    .withBody(chatDto.getBody())
                    .withCreatorName(chatDto.getCreatorName())
                    .withCreator(chatDto.getCreator())
                    .withMessageId(chatDto.getMessageId())
                    .withDateCreated(chatDto.getDateCreated())
                    .withChatWindowId(chatDto.getChatWindowId())
                    .build();
            i++;
        }
        Thread offlineChatStoreThread = new Thread(new OfflineChatStoreThread(offlineChatDtoList));
        offlineChatStoreThread.start();
    }

    public LiveData<List<OfflineChatDto>> getChatMessages(String chatWindowId) {
        offlineChatDtoLiveData = OfflineChatDatabaseInitializer.findByChatWindowId(OfflineChatMessageDatabase.getAppDatabase(
                getAppContext()), chatWindowId);
        getChatMessagesFromFirebase(chatWindowId);
        return offlineChatDtoLiveData;
    }

}
