package com.example.myapplication;

public class ContactItem {
    //private Integer Img;
    private String ID;
    private Boolean close;
    private Integer money;

    public ContactItem(){}

    public ContactItem(String id, Boolean close, Integer money){
        this.ID = id;
       // this.Img = img;
        this.close = close;
        this.money = money;
    }

    public String getId(){
        return ID;
    }

    public Boolean getClose(){ return close; }


    public Integer getMoney(){
        return money;
    }
    //public Integer getImg(){
      //  return Img;
    //}
}