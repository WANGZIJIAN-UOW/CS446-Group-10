package com.example.myapplication;

public class Loans {
    public String Amount;
    public String Creditor;
    public String Debtor;
    /*public Loans(String Amount,String Creditor,String Debtor){
        this.Amount = Amount;
        this.Creditor = Creditor;
        this.Debtor = Debtor;
    }*/
    public Loans(){
    }


    public String toString(){
        return "$" + this.Amount + " to " + this.Creditor;
    }
}
