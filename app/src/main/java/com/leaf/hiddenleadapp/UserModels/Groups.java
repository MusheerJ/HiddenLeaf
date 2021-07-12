package com.leaf.hiddenleadapp.UserModels;

public class Groups
{
    String groupName, groupProfile, groupDescription ,groupId;

    public Groups(String groupName, String groupProfile, String groupDescription, String groupId) {
        this.groupName = groupName;
        this.groupProfile = groupProfile;
        this.groupDescription = groupDescription;
        this.groupId = groupId;
    }

    public Groups() {

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupProfile() {
        return groupProfile;
    }

    public void setGroupProfile(String groupProfile) {
        this.groupProfile = groupProfile;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
