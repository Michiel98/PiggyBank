package com.piggebank.paybackPackage;

public class PayBackObject {

    public String date;
    public double price;
    public boolean borrowed;
    public String uidFriend;

    public PayBackObject(){

    }

    public PayBackObject(String date, double price,boolean borrowed,String uidfriend) {
        this.uidFriend = uidfriend;
        this.price = price;
        this.borrowed=borrowed;
        this.date=date;
    }


    public double getPrice() {
        return price;
    }
    public boolean getBorrowed(){return borrowed;}
    public String getDate(){return date;}
    public String getuidFriend() {
        return uidFriend;
    }
}
