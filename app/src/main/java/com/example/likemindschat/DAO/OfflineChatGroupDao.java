package com.example.likemindschat.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.likemindschat.entities.OfflineChatWindowDto;

import java.util.List;

@Dao
public interface OfflineChatGroupDao {

    @Query("SELECT * FROM offlineChatWindowDto")
    LiveData<List<OfflineChatWindowDto>> findAllGroups();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(
            OfflineChatWindowDto... offlineChatGroupDaos);
}
