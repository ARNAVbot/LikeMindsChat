package com.example.likemindschat.database;


import androidx.lifecycle.LiveData;

import com.example.likemindschat.entities.OfflineChatDto;
import com.example.likemindschat.entities.OfflineChatWindowDto;

import java.util.List;


public class OfflineChatDatabaseInitializer {

    public static void addResponses(final OfflineChatMessageDatabase offlineChatMessageDatabase, OfflineChatDto[] offlineChatDtos) {
        offlineChatMessageDatabase.offlineChatDtoDao().insert(offlineChatDtos);
    }

    public static LiveData<List<OfflineChatDto>> findByChatWindowId(final OfflineChatMessageDatabase offlineChatMessageDatabase, String chatWindowId) {
        return offlineChatMessageDatabase.offlineChatDtoDao().findByChatWindowId(chatWindowId);
    }

    public static void addChatGroupResponse(final OfflineChatGroupDatabase offlineChatGroupDatabase, OfflineChatWindowDto[] offlineChatWindowDtos) {
        offlineChatGroupDatabase.offlineChatGroupDao().insert(offlineChatWindowDtos);
    }

    public static LiveData<List<OfflineChatWindowDto>> fetchAllChatGroups(final OfflineChatGroupDatabase offlineChatGroupDatabase) {
        return offlineChatGroupDatabase.offlineChatGroupDao().findAllGroups();
    }
}
