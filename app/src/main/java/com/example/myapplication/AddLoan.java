package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddLoan extends AppCompatActivity {

    private EditText loanName;
    private EditText loanMoney;
    private Button confirmLoan;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan2);
        username = getIntent().getExtras().getString("username");
        confirmLoan = (Button) findViewById(R.id.confirmLoan);
        confirmLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanName = (EditText) findViewById(R.id.loanName);
                loanMoney = (EditText) findViewById(R.id.loanMoney);
                addNewLoan2db(username,loanName.getText().toString(), loanMoney.getText().toString());
            }
        });

    }
    public void addNewLoan2db(String username,String name, String amount){


        Map<String, Object> data = new HashMap<>();
        data.put("Amount", amount);
        data.put("Creditor", name);
        data.put("Debtor", username);

        db.collection("Loans").add(data);
        Intent intent = new Intent(this, AddLoanActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);

    }
}
