package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class ShowBalance extends AppCompatActivity {

    public static class Contact {
        public String contact;
        public boolean close;
        public double money;
        public Contact() { }
    }

    private static final String TAG = SignUpActivity.class.getName();
    private String username;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListenerl;
    private CollectionReference mDocRef;
    public ArrayList<Contact> contacts = new ArrayList<Contact>();

    private Button loans;
    private Button gosearch;
    private Button logout;

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

        logout = findViewById(R.id.logOut1);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(ShowBalance.this, MainActivity.class);
                startActivity(intToMain);
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

                            TextView owedBalance = (TextView)findViewById(R.id.owedBalance);
                            String owedBalanceAmount = "$" + getOwedBalance();
                            owedBalance.setText(owedBalanceAmount);

                            TextView outstandingBalance = (TextView)findViewById(R.id.outstandingBalance);
                            String outstandingBalanceAmount = "$" + getOutstandingBalance();
                            outstandingBalance.setText(outstandingBalanceAmount);
                        }
                    }
                }
            }
        });
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

}