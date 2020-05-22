package com.example.register;

public class CommunityReplyItem {

    String nickname;
    String reply;
    String date;
    String password;

    public CommunityReplyItem(String nickname, String reply, String date,String password) {
        this.nickname = nickname;
        this.reply = reply;
        this.date = date;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "com.example.register.CommunityContentActivity.CommunityReplyItem{" +
                "nickname='" + nickname + '\'' +
                ", reply='" + reply + '\'' +
                ", date='" + date + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}