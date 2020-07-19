package com.example.likemindschat.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.likemindschat.DAO.OfflineChatDtoDao;
import com.example.likemindschat.entities.OfflineChatDto;

@Database(entities = {OfflineChatDto.class}, version = 1, exportSchema = false)
public abstract class OfflineChatMessageDatabase extends RoomDatabase {

    private static OfflineChatMessageDatabase INSTANCE;

    public abstract OfflineChatDtoDao offlineChatDtoDao();

    public static OfflineChatMessageDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), OfflineChatMessageDatabase.class,
                            "chat-database")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
