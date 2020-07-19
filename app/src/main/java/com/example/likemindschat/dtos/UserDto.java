package com.example.likemindschat.dtos;

import java.io.Serializable;
import java.util.List;

public class UserDto implements Serializable {

    private String userName;
    private String uuid;
    private boolean isChecked;
    private List<String> chatWindowIds;

    public UserDto() {
    }

    public UserDto(String userName, String uuid, boolean isChecked, List<String> chatWindowIds) {
        this.userName = userName;
        this.uuid = uuid;
        this.isChecked = isChecked;
        this.chatWindowIds = chatWindowIds;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<String> getChatWindowIds() {
        return chatWindowIds;
    }

    public void setChatWindowIds(List<String> chatWindowIds) {
        this.chatWindowIds = chatWindowIds;
    }

    public static interface UserNameStep {
        UuidStep withUserName(String userName);
    }

    public static interface UuidStep {
        IsCheckedStep withUuid(String uuid);
    }

    public static interface IsCheckedStep {
        ChatWindowIdsStep withIsChecked(boolean isChecked);
    }

    public static interface ChatWindowIdsStep {
        BuildStep withChatWindowIds(List<String> chatWindowIds);
    }

    public static interface BuildStep {
        UserDto build();
    }

    public static class Builder implements UserNameStep, UuidStep, IsCheckedStep, ChatWindowIdsStep, BuildStep {
        private String userName;
        private String uuid;
        private boolean isChecked;
        private List<String> chatWindowIds;

        private Builder() {
        }

        public static UserNameStep userDto() {
            return new Builder();
        }

        @Override
        public UuidStep withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        @Override
        public IsCheckedStep withUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        @Override
        public ChatWindowIdsStep withIsChecked(boolean isChecked) {
            this.isChecked = isChecked;
            return this;
        }

        @Override
        public BuildStep withChatWindowIds(List<String> chatWindowIds) {
            this.chatWindowIds = chatWindowIds;
            return this;
        }

        @Override
        public UserDto build() {
            return new UserDto(
                    this.userName,
                    this.uuid,
                    this.isChecked,
                    this.chatWindowIds
            );
        }
    }
}
