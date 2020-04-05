package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyWallet extends AppCompatActivity {

    public static class wallet {
        public String contact;
        public double amount;
        public wallet() { }
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView WalletMoney;
    private Button AddMoney;
    private Button Withdraw;
    private EditText input;
    private EditText input2;
    private String username;
    public double moneyAmount = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        username = getIntent().getExtras().getString("email");

        AddMoney = (Button)findViewById(R.id.addmoney);
        Withdraw = (Button)findViewById(R.id.withdraw);
        WalletMoney = (TextView)findViewById(R.id.walletmoney);

        setWalletAmount();
    }

    public void setWalletAmount() {
        db.collection("wallet").document(username)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                wallet userWallet = new wallet();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userWallet = document.toObject(wallet.class);
                    }
                }
                moneyAmount = userWallet.amount;
                WalletMoney.setText(Double.toString(moneyAmount));
                addMoney();
                removeMoney();
            }
        });
    }

    public void addMoney() {
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
                moneyAmount = Double.valueOf(moneyAmount) + Double.valueOf(txt);
                WalletMoney.setText(Double.toString(moneyAmount));

                Map<String, Object> dataToUpdate = new HashMap<String, Object>();
                dataToUpdate.put("amount", moneyAmount);
                db.collection("wallet").document(username).update(dataToUpdate);
            }
        });
        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog ad = builder1.create();
        AddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });
    }

    public void removeMoney() {
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
                moneyAmount = Double.valueOf(moneyAmount) - Double.valueOf(txt);
                WalletMoney.setText(Double.toString(moneyAmount));

                Map<String, Object> dataToUpdate = new HashMap<String, Object>();
                dataToUpdate.put("amount", moneyAmount);
                db.collection("wallet").document(username).update(dataToUpdate);
            }
        });
        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog adw = builder2.create();
        Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adw.show();
            }
        });
    }
}
