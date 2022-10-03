package com.piggebank.uitgavenOverzicht;

public class uitgaven {

    String catergory;
    double price;
    String date;

    public uitgaven(String catergory, double price, String date){
        this.catergory=catergory;
        this.price=price;
        this.date=date;
    }

    public uitgaven(){

    }

    public String getCatergory() {
        return catergory;
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
}
