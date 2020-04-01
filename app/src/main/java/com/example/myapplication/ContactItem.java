package com.example.myapplication;

public class ContactItem {
    //private Integer Img;
    private String name;
    private Boolean close;
    private Integer money;

    public ContactItem(){}

    public ContactItem(String name, Boolean close, Integer money){
        this.name = name;
       // this.Img = img;
        this.close = close;
        this.money = money;
    }

    public String getName(){
        return name;
    }

    public Boolean getClose(){ return close; }


    public Integer getMoney(){
        return money;
    }
    //public Integer getImg(){
      //  return Img;
    //}
}