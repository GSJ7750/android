package com.example.register;

//////////////조회쿼리 변경부분 ////////////////////
public class User {
    String subject;
    String place;
    String lost_ID;
    String details;
    String time;
    String time2;
    String time3;
    String Latitude;
    String Longitude;
    String url;
    String password;

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    public String getLost_ID() {
        return lost_ID;
    }
    public void setLost_ID(String lost_ID) {
        this.lost_ID = "물품번호 : "+lost_ID;
    }

    public String getDetails() { return details;}
    public  void setDetails(String details) {this.details = details; }

    public  String getTime() {return time; }
    public void setTime(String time) {this.time = time;}

    public String getTime2() {return time2; }
    public void setTime2(String time) {this.time2 = time2;}

    public String getTime3() {return time3; }
    public void setTime3(String time) {this.time3 = time3;}

    public String getLatitude() {return Latitude; }
    public void setLatitude(String Latitude) {this.Latitude = Latitude;}

    public String getLongitude() {return Longitude; }
    public void setLongitude(String Longitude) {this.Longitude = Longitude;}

    public String getUrl() {return url; }
    public void setUrl(String url) {this.url = url;}

    public String getPassword() {return password; }
    public void setPassword(String password) {this.password = password;}

    public String getMDT()
    {
        String MDT = time.toString()+time2.toString()+time3.toString();
        return MDT;
    }

    public User(String subject, String place,String time,String time2, String time3, String details, String lost_ID, String Latitude, String Longitude,
                String url, String password) {
        this.subject = subject;
        this.place = place;
        this.time = time;
        this.time2 = time2;
        this.time3 = time3;
        this.details = details;
        this.lost_ID = lost_ID;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.url = url;
        this.password = password;

    }

}
