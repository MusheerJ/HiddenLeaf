package com.leaf.hiddenleadapp.UserModels;

public class Users
{
    String ProfilePic , userName , mail , passWord , lastMessage,PhoneNumber;
    String UserBio;
    String UserId;

    public Users(){}

    public String getUserBio() {
        return UserBio;
    }

    public void setUserBio(String userBio) {
        UserBio = userBio;
    }

    public Users(String profilePic, String userBio) {
        ProfilePic = profilePic;
        UserBio = userBio;
    }

    public Users(String profilePic, String userName, String mail, String passWord, String lastMessage)
    {
        this.ProfilePic = profilePic;
        this.userName = userName;
        this.mail = mail;
        this.passWord = passWord;
        this.lastMessage = lastMessage;
    }

    //Phone Auth Constructor
    public Users(String profilePic,String userName,String PhoneNumber,String UserId,int a){
        this.ProfilePic = profilePic;
        this.userName = userName;
        this.UserId = UserId ;
        this.PhoneNumber = PhoneNumber;
    }

    // Sign Up constructor
    public Users( String userName, String mail, String passWord,String UserId)
    {
        this.userName = userName;
        this.mail = mail;
        this.passWord = passWord;
        this.UserId = UserId;
    }



    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}