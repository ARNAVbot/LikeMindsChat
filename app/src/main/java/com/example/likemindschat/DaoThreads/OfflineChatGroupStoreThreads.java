package com.example.likemindschat.DaoThreads;

import com.example.likemindschat.database.OfflineChatDatabaseInitializer;
import com.example.likemindschat.database.OfflineChatGroupDatabase;
import com.example.likemindschat.entities.OfflineChatWindowDto;

import static com.example.likemindschat.App.getAppContext;

public class OfflineChatGroupStoreThreads implements Runnable {

    private OfflineChatWindowDto[] offlineChatWindowDtos;

    public OfflineChatGroupStoreThreads(OfflineChatWindowDto[] offlineChatWindowDtos) {
        this.offlineChatWindowDtos = offlineChatWindowDtos;
    }

    @Override
    public void run() {
        if(offlineChatWindowDtos == null || offlineChatWindowDtos.length == 0) {
            return;
        }
        OfflineChatDatabaseInitializer.addChatGroupResponse(
                OfflineChatGroupDatabase.getAppDatabase(
                        getAppContext()), offlineChatWindowDtos);
    }
}
