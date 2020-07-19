package com.example.likemindschat.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "offlineChatDo")
public class OfflineChatDto {

    @NonNull
    @PrimaryKey private String messageId;

    @ColumnInfo(name = "body") private String body;

    @ColumnInfo(name = "creatorName") private String creatorName;

    @ColumnInfo(name = "creator") private String creator;

    @ColumnInfo(name = "dateCreated") private long dateCreated;

    @ColumnInfo(name = "chatWindowId") private String chatWindowId;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getChatWindowId() {
        return chatWindowId;
    }

    public void setChatWindowId(String chatWindowId) {
        this.chatWindowId = chatWindowId;
    }

    public static interface BodyStep {
        CreatorNameStep withBody(String body);
    }

    public static interface CreatorNameStep {
        CreatorStep withCreatorName(String creatorName);
    }

    public static interface CreatorStep {
        MessageIdStep withCreator(String creator);
    }

    public static interface MessageIdStep {
        DateCreatedStep withMessageId(String messageId);
    }

    public static interface DateCreatedStep {
        ChatWindowIdStep withDateCreated(long dateCreated);
    }

    public static interface ChatWindowIdStep {
        BuildStep withChatWindowId(String chatWindowId);
    }

    public static interface BuildStep {
        OfflineChatDto build();
    }

    public static class Builder implements BodyStep, CreatorNameStep, CreatorStep, MessageIdStep, DateCreatedStep, ChatWindowIdStep, BuildStep {
        private String body;
        private String creatorName;
        private String creator;
        private String messageId;
        private long dateCreated;
        private String chatWindowId;

        private Builder() {
        }

        public static BodyStep offlineChatDto() {
            return new Builder();
        }

        @Override
        public CreatorNameStep withBody(String body) {
            this.body = body;
            return this;
        }

        @Override
        public CreatorStep withCreatorName(String creatorName) {
            this.creatorName = creatorName;
            return this;
        }

        @Override
        public MessageIdStep withCreator(String creator) {
            this.creator = creator;
            return this;
        }

        @Override
        public DateCreatedStep withMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        @Override
        public ChatWindowIdStep withDateCreated(long dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }

        @Override
        public BuildStep withChatWindowId(String chatWindowId) {
            this.chatWindowId = chatWindowId;
            return this;
        }

        @Override
        public OfflineChatDto build() {
            OfflineChatDto offlineChatDto = new OfflineChatDto();
            offlineChatDto.setBody(this.body);
            offlineChatDto.setCreatorName(this.creatorName);
            offlineChatDto.setCreator(this.creator);
            offlineChatDto.setMessageId(this.messageId);
            offlineChatDto.setDateCreated(this.dateCreated);
            offlineChatDto.setChatWindowId(this.chatWindowId);
            return offlineChatDto;
        }
    }
}
