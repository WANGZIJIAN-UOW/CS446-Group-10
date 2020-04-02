package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyWallet extends AppCompatActivity {

    private TextView WalletMoney;
    private Button AddMoney;
    private Button Withdraw;
    private EditText input;
    private EditText input2;
    public String moneyAmount = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        AddMoney = (Button)findViewById(R.id.addmoney);
        Withdraw = (Button)findViewById(R.id.withdraw);
        WalletMoney = (TextView)findViewById(R.id.walletmoney);

        WalletMoney.setText(moneyAmount);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Amount Value");
        builder1.setIcon(R.drawable.ic_launcher_background);
        builder1.setMessage("Please enter the amount of money you wish to add");

        input = new EditText(this);
        builder1.setView(input);

        builder1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = input.getText().toString();
                moneyAmount = String.valueOf(Double.valueOf(moneyAmount) + Double.valueOf(txt));
                WalletMoney.setText(moneyAmount);
            }
        });
        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });





        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Amount Value");
        //builder1.setIcon(R.drawable.ic_launcher_background);
        builder2.setMessage("Please enter the amount of money you wish to withdraw");

        input2 = new EditText(this);
        builder2.setView(input2);

        builder2.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = input2.getText().toString();
                moneyAmount = String.valueOf(Double.valueOf(moneyAmount) - Double.valueOf(txt));
                WalletMoney.setText(moneyAmount);
            }
        });
        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        final AlertDialog ad = builder1.create();
        final AlertDialog adw = builder2.create();
        AddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });
        Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adw.show();
            }
        });
    }
}
