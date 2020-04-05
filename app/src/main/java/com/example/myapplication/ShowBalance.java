package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowBalance extends AppCompatActivity {

    public static class Contact {
        public String contact;
        public boolean close;
        public double money;
        public double wallet = 0.0;
        public Contact() { }
    }

    public static class wallet {
        public String contact;
        public double amount;
        public wallet() { }
    }

    public Map<String, Object> updateDebt(boolean close, double money) {
        Map<String, Object> dataToUpdate = new HashMap<String, Object>();
        dataToUpdate.put("close", true);
        dataToUpdate.put("money", money);
        return dataToUpdate;
    }

    private static final String TAG = SignUpActivity.class.getName();
    private String username;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mDocRef;
    public ArrayList<Contact> contacts = new ArrayList<Contact>();

    private Button loans;
    private Button gosearch;
    private Button logout;
    private Button payDebts;
    private Button history;
    private Button wallet;
    private Button owedBalance;
    private Button outstandingBalance;
    private ImageButton setting;

    public double getOwedBalance() {
        double positive = 0.0;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).money > 0) {
                positive += contacts.get(i).money;
            }
        }
        return positive;
    }

    public double getOutstandingBalance() {
        double negative = 0.0;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).money < 0) {
                negative += (contacts.get(i).money * -1);
            }
        }
        return negative;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_balance);
        username = getIntent().getExtras().getString("email");
        mDocRef = db.collection("contact/" + username + "/list");

        owedBalance = (Button)findViewById(R.id.owedBalance);
        outstandingBalance = (Button)findViewById(R.id.outstandingBalance);

        fillBalances();

        loans = (Button) findViewById(R.id.loans);
        loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoans();
            }
        });

        gosearch = findViewById(R.id.gosearch);
        gosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoSearch();
            }
        });

        history = findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistory();
            }
        });

        setting = findViewById(R.id.settingBtn);
        setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                GoSetting();
            }
        });

        wallet = findViewById(R.id.myWallet);
        wallet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openWallet();
            }
        });

        logout = findViewById(R.id.logOut1);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowBalance.this, "You've logged out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(ShowBalance.this, MainActivity.class);
                startActivity(intToMain);
            }
        });

        payDebts = (Button)findViewById(R.id.payDebts);
        payDebts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOutstandingBalance() == 0.0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ShowBalance.this).create();
                    alertDialog.setTitle("Info Screen");
                    alertDialog.setMessage("You have no debts to settle!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(ShowBalance.this).create();
                    alertDialog.setTitle("Confirmation Screen");
                    alertDialog.setMessage("Do you wish to settle all your debts?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    setWallets();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }

    public void fillBalances() {
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            Contact contact = document.toObject(Contact.class);
                            contact.contact = document.getId();
                            if (!document.getId().equals(username))
                                contacts.add(contact);

                            String owedBalanceAmount = "$" + getOwedBalance();
                            owedBalance.setText(owedBalanceAmount);
                            if (getOwedBalance() != 0.0) {
                                owedBalance.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openFullBalance("Owed");
                                    }
                                });
                            }
                            String outstandingBalanceAmount = "$" + getOutstandingBalance();
                            outstandingBalance.setText(outstandingBalanceAmount);
                            if (getOutstandingBalance() != 0.0) {
                                outstandingBalance.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openFullBalance("Outstanding");
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    public void setWallets() {
        db.collection("wallet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            wallet wallet = document.toObject(wallet.class);

                            for (int i = 0; i < contacts.size(); i++) {
                                if (document.getId().equals(contacts.get(i).contact)) {
                                    contacts.get(i).wallet = wallet.amount;
                                }
                            }
                        }
                    }
                    payDebts();
                }
            }
        });
    }

    public void payDebts() {
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
            payDebtsBalance(userWallet);
            }
        });
    }

    public void payDebtsBalance(wallet wallet) {
        double money = 0.0;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).money < 0) {
                money += (contacts.get(i).money * -1);
            }
        }

        if (wallet.amount >= money) {
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).money < 0) {
                    db.collection("contact").document(username)
                            .collection("list").document(contacts.get(i).contact).update(updateDebt(true,  0));
                    db.collection("contact").document(contacts.get(i).contact)
                            .collection("list").document(username).update(updateDebt(true, 0));

                    Map<String, Object> positive = new HashMap<String, Object>();
                    positive.put("amount", contacts.get(i).wallet + (contacts.get(i).money *  -1));
                    db.collection("wallet").document(contacts.get(i).contact).update(positive);
                }
            }
            Map<String, Object> negative = new HashMap<String, Object>();
            negative.put("amount", wallet.amount - money);
            db.collection("wallet").document(username).update(negative);

            Intent intent = new Intent(this, ShowBalance.class);
            intent.putExtra("email", username);
            startActivity(intent);
        } else {
            Toast.makeText(ShowBalance.this, "You don't have enough money!", Toast.LENGTH_SHORT).show();
        }

    }

    public void openLoans(){
        Intent intent = new Intent(this, AddLoanActivity.class);
        intent.putExtra("email", username);
        startActivity(intent);
    }

    public void GoSearch(){
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("email", username);
        startActivity(intent);
    }
    public void GoSetting(){
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("email", username);
        startActivity(intent);
    }

    public void openFullBalance(String status){
        Intent intent = new Intent(this, FullBalance.class);
        intent.putExtra("email", username);
        intent.putExtra("status", status);
        startActivity(intent);
    }

    public void openHistory(){
        Intent intent = new Intent(this, HistoryLoans.class);
        intent.putExtra("email", username);
        startActivity(intent);
    }

    public void openWallet() {
        Intent intent = new Intent(this, MyWallet.class);
        intent.putExtra("email", username);
        startActivity(intent);
    }

}