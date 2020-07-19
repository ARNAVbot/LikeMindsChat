package com.example.likemindschat.DAO;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.likemindschat.entities.OfflineChatDto;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface OfflineChatDtoDao {

    @Query("SELECT * FROM offlineChatDo where chatWindowId = :chatWindowId")
    LiveData<List<OfflineChatDto>> findByChatWindowId(String chatWindowId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(
            OfflineChatDto... offlineChatDtos);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(
            OfflineChatDto offlineChatDto);

    @Query("DELETE FROM offlineChatDo where messageId = :messageId")
    void deleteOfflineChatDtoByMessageId(
            String messageId);

    @Query("DELETE FROM offlineChatDo where chatWindowId = :chatWindowId")
    void deleteOfflineChatDtoByChatWindowId(
            String chatWindowId);

    @Query("DELETE FROM offlineChatDo ")
    void deleteAll();
}
