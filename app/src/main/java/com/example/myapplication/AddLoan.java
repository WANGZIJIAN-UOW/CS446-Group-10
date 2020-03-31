package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;

public class AddLoan extends AppCompatActivity {

    private EditText loanName;
    private EditText loanMoney;
    private Button confirmLoan;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String username;

    Table<String, String, Contact> contactMap = HashBasedTable.create();
    ArrayList<String> names = new ArrayList<String>();

    public static class Contact {
        public String contact;
        public boolean close;
        public double money;
        public Contact() { }
    }

    public Map<String, Object> updateDebt(boolean close, double money) {
        Map<String, Object> dataToUpdate = new HashMap<String, Object>();
        dataToUpdate.put("close", true);
        dataToUpdate.put("money", money);
        return dataToUpdate;
    }

    public void addDebt(final String receive, final String send, final double value) {
        db.collection("contact").document(receive)
                .collection("list").document(send).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Contact contact =  new Contact();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        contact = document.toObject(Contact.class);
                    }
                }
                db.collection("contact").document(receive)
                        .collection("list").document(send).update(updateDebt(true,  contact.money + value));
                db.collection("contact").document(send)
                        .collection("list").document(receive).update(updateDebt(true, (contact.money * -1) + (value * -1)));
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan2);
        username = getIntent().getExtras().getString("email");
        confirmLoan = (Button) findViewById(R.id.confirmLoan);
        confirmLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanName = (EditText) findViewById(R.id.loanName);
                loanMoney = (EditText) findViewById(R.id.loanMoney);
                if(!loanName.getText().toString().isEmpty() && !loanMoney.getText().toString().isEmpty()) {
                    addNewLoan2db(username, loanName.getText().toString(), loanMoney.getText().toString());
                }
            }
        });

    }
    public void addNewLoan2db(String username,String name, String amount){


        Map<String, Object> data = new HashMap<>();
        data.put("Amount", amount);
        data.put("Creditor", name);
        data.put("Debtor", username);

        db.collection("Loans").add(data);

        addDebt(username, name, Double.parseDouble(amount));

        Intent intent = new Intent(this, AddLoanActivity.class);
        intent.putExtra("email", username);
        startActivity(intent);

    }
}
