package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class ShowBalance extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mDocRef = db.document("contact/shanglin1/list/shanglin2");

    public static class Contact {
        public boolean close;
        public int money;
        public Contact() { }

        public void addMoney(int moneyValue) {
            money += moneyValue;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_balance);

        TextView owedBalance = (TextView)findViewById(R.id.owedBalance);
        owedBalance.setText("$32.25");

        fillBalances();
    }

    public void fillBalances() {
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Contact contact = document.toObject(Contact.class);
                        String money = Integer.toString(contact.money);
                        TextView outstandingBalance = (TextView)findViewById(R.id.outstandingBalance);
                        outstandingBalance.setText(money);
                    }
                }
            }
        });
    }
}