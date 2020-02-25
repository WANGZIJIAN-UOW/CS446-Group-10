package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private String username = "shanglin2";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mDocRef = db.collection("contact/" + username + "/list");
    public ArrayList<Contact> contacts = new ArrayList<Contact>();

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
        fillBalances();
    }

    public void fillBalances() {
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            Contact contact = document.toObject(Contact.class);
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
}