package com.example.register;

public class PublicData {
    String num;
    String status;
    String regDate;
    String recivedDate;
    String categoryName;
    String category;
    String zone;
    String company;
    String detail;
    String place;



    public String getnum() {
        return num;
    }
    public void setnum(String num) {
        this.num = num;
    }

    public String getstatus() {
        return status;
    }
    public void setstatus(String status) {
        this.status = status;
    }

    public String getregDate() {
        return regDate;
    }
    public void setregDate(String regDate) {
        this.regDate = regDate;
    }

    public String getrecivedDate() {
        return recivedDate;
    }
    public void setrecivedDate(String recivedDate) {
        this.recivedDate = recivedDate;
    }

    public String getcategoryName() {
        return categoryName;
    }
    public void setcategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getcategory() {
        return category;
    }
    public void setcategory(String category) {
        this.category = category;
    }

    public String getzone() {
        return zone;
    }
    public void setzone(String zone) {
        this.zone = zone;
    }


    public String getcompany() {
        return company;
    }
    public void setcompany(String company) {
        this.company = company;
    }


    public String getdetail() {
        return detail;
    }
    public void setdetail(String detail) {
        this.detail = detail;
    }

    public String getplace() {
        return place;
    }
    public void setdplace(String place) {
        this.place = place;
    }




    public PublicData(String num, String regDate, String recivedDate, String status, String categoryName,String category ,String zone ,String company ,String detail, String place)
    {
        this.num = num;
        this.regDate = regDate;
        this.recivedDate = recivedDate;
        this.status = status;
        this.categoryName = categoryName;
        this.category = category;
        this.zone =zone;
        this.company =company;
        this.detail = detail;
        this.place = place;
    }
}
