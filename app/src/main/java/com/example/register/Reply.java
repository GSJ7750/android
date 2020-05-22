package com.example.register;

public class Reply {
    String lost_ID;
    String ReplyIndex;
    String ID;
    String Password;
    String Contents;
    String Admin;

    public String getlost_ID() {
        return lost_ID;
    }
    public void setlost_ID(String lost_ID) {
        this.lost_ID = lost_ID;
    }

    public String getReplyIndex() {
        return ReplyIndex;
    }
    public void setReplyIndex(String ReplyIndex) {
        this.ReplyIndex = ReplyIndex;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() { return Password; }
    public void setPassword(String Password) { this.Password = Password; }

    public String getContents() {
        return Contents;
    }
    public void setContents(String Contents) {
        this.Contents = Contents;
    }

    public String getAdmin() {
        return Admin;
    }
    public void setAdmin(String Admin) {
        this.Admin = Admin;
    }

    public Reply(String lost_ID, String ID, String Password, String ReplyIndex, String Contents, String Admin)
    {
        this.lost_ID = lost_ID;
        this.ID = ID;
        this.Password = Password;
        this.ReplyIndex = ReplyIndex;
        this.Contents = Contents;
        this.Admin = Admin;
    }
}
