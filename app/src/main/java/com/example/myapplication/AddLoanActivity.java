package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddLoanActivity extends AppCompatActivity {
    private Button addLoan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);

        addLoan = (Button) findViewById(R.id.addLoan);
        addLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddLoan();
            }
        });

    }
    public void openAddLoan(){
        Intent intent = new Intent(this, AddLoan.class);
        startActivity(intent);
    }

}
