package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddLoan extends AppCompatActivity {

    private EditText loanName;
    private EditText loanMoney;
    private Button confirmLoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan2);
        confirmLoan = (Button) findViewById(R.id.addLoan);
        confirmLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanName = (EditText) findViewById(R.id.loanName);
                loanMoney = (EditText) findViewById(R.id.loanMoney);
                addNewLoan2db(loanName.getText().toString(), loanMoney.getText().toString());
            }
        });

    }
    public void addNewLoan2db(String name, String amount){

    }
}
