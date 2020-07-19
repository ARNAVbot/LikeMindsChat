package com.example.likemindschat.dtos;

import java.io.Serializable;

public class ChatDto implements Serializable {
    private String body;
    private String creatorName;
    private String creator;
    private long dateCreated;
    private String messageId;
    private String chatWindowId;

    public ChatDto() {
    }

    public ChatDto(String body, String creatorName, String creator, long dateCreated, String messageId, String chatWindowId) {
        this.body = body;
        this.creatorName = creatorName;
        this.creator = creator;
        this.dateCreated = dateCreated;
        this.messageId = messageId;
        this.chatWindowId = chatWindowId;
    }

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

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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
        DateCreatedStep withCreator(String creator);
    }

    public static interface DateCreatedStep {
        MessageIdStep withDateCreated(long dateCreated);
    }

    public static interface MessageIdStep {
        ChatWindowIdStep withMessageId(String messageId);
    }

    public static interface ChatWindowIdStep {
        BuildStep withChatWindowId(String chatWindowId);
    }

    public static interface BuildStep {
        ChatDto build();
    }

    public static class Builder implements BodyStep, CreatorNameStep, CreatorStep, DateCreatedStep, MessageIdStep, ChatWindowIdStep, BuildStep {
        private String body;
        private String creatorName;
        private String creator;
        private long dateCreated;
        private String messageId;
        private String chatWindowId;

        private Builder() {
        }

        public static BodyStep chatDto() {
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
        public DateCreatedStep withCreator(String creator) {
            this.creator = creator;
            return this;
        }

        @Override
        public MessageIdStep withDateCreated(long dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }

        @Override
        public ChatWindowIdStep withMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        @Override
        public BuildStep withChatWindowId(String chatWindowId) {
            this.chatWindowId = chatWindowId;
            return this;
        }

        @Override
        public ChatDto build() {
            return new ChatDto(
                    this.body,
                    this.creatorName,
                    this.creator,
                    this.dateCreated,
                    this.messageId,
                    this.chatWindowId
            );
        }
    }
}
