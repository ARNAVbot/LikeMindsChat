package com.example.likemindschat.DaoThreads;

import com.example.likemindschat.database.OfflineChatDatabaseInitializer;
import com.example.likemindschat.database.OfflineChatMessageDatabase;
import com.example.likemindschat.entities.OfflineChatDto;

import static com.example.likemindschat.App.getAppContext;

public class OfflineChatStoreThread implements Runnable {

    private OfflineChatDto[] offlineChatDtoList;

    public OfflineChatStoreThread(OfflineChatDto[] offlineChatDtoList) {
        this.offlineChatDtoList = offlineChatDtoList;
    }

    @Override
    public void run() {
        if(offlineChatDtoList == null || offlineChatDtoList.length == 0) {
            return;
        }
        OfflineChatDatabaseInitializer.addResponses(
                OfflineChatMessageDatabase.getAppDatabase(
                        getAppContext()), offlineChatDtoList);
    }
}
