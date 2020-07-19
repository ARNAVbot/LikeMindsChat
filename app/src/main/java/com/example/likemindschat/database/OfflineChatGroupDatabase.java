package com.example.likemindschat.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.likemindschat.DAO.OfflineChatGroupDao;
import com.example.likemindschat.entities.OfflineChatWindowDto;

@Database(entities = {OfflineChatWindowDto.class}, version = 1, exportSchema = false)
public abstract class OfflineChatGroupDatabase extends RoomDatabase {

    private static OfflineChatGroupDatabase INSTANCE;

    public abstract OfflineChatGroupDao offlineChatGroupDao();

    public static OfflineChatGroupDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), OfflineChatGroupDatabase.class,
                            "chat-group-database")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
