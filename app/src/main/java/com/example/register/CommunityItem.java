package com.example.register;

import java.io.Serializable;

public class CommunityItem implements Serializable {
    String title;
    String content;
    String writetime;
    String userid;
    String password;
    public CommunityItem(String title, String content, String writetime, String userid, String password) {
        this.title = title;
        this.content = content;
        this.writetime = writetime;
        this.userid = userid;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWritetime() {
        return writetime;
    }

    public void setWritetime(String writetime) {
        this.writetime = writetime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CommunityItem{" +
                "title='" + title + '\'' +
                ", content='" + content + '\''+
                ", writetime='" + writetime + '\''+
                ", userid='" + userid + '\''+
                ", password='" + password + '\''+'}';
    }

}
