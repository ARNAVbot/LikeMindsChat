package com.example.likemindschat.dtos;

import java.io.Serializable;

public class ChatWindowDto implements Serializable {

    private String chatWindowId;
    private String creator;
    private String title;
    private long createdTime;
    private String creatorName;

    public ChatWindowDto() {
    }

    public ChatWindowDto(String chatWindowId, String creator, String title, long createdTime, String creatorName) {
        this.chatWindowId = chatWindowId;
        this.creator = creator;
        this.title = title;
        this.createdTime = createdTime;
        this.creatorName = creatorName;
    }

    public String getChatWindowId() {
        return chatWindowId;
    }

    public void setChatWindowId(String chatWindowId) {
        this.chatWindowId = chatWindowId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public static interface ChatWindowIdStep {
        CreatorStep withChatWindowId(String chatWindowId);
    }

    public static interface CreatorStep {
        TitleStep withCreator(String creator);
    }

    public static interface TitleStep {
        CreatedTimeStep withTitle(String title);
    }

    public static interface CreatedTimeStep {
        CreatorNameStep withCreatedTime(long createdTime);
    }

    public static interface CreatorNameStep {
        BuildStep withCreatorName(String creatorName);
    }

    public static interface BuildStep {
        ChatWindowDto build();
    }

    public static class Builder implements ChatWindowIdStep, CreatorStep, TitleStep, CreatedTimeStep, CreatorNameStep, BuildStep {
        private String chatWindowId;
        private String creator;
        private String title;
        private long createdTime;
        private String creatorName;

        private Builder() {
        }

        public static ChatWindowIdStep chatWindowDto() {
            return new Builder();
        }

        @Override
        public CreatorStep withChatWindowId(String chatWindowId) {
            this.chatWindowId = chatWindowId;
            return this;
        }

        @Override
        public TitleStep withCreator(String creator) {
            this.creator = creator;
            return this;
        }

        @Override
        public CreatedTimeStep withTitle(String title) {
            this.title = title;
            return this;
        }

        @Override
        public CreatorNameStep withCreatedTime(long createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        @Override
        public BuildStep withCreatorName(String creatorName) {
            this.creatorName = creatorName;
            return this;
        }

        @Override
        public ChatWindowDto build() {
            return new ChatWindowDto(
                    this.chatWindowId,
                    this.creator,
                    this.title,
                    this.createdTime,
                    this.creatorName
            );
        }
    }
}
