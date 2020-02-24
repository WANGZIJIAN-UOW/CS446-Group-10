package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity{
    private static final String TAG = "secondActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Button btn1 = this.findViewById(R.id.friend);
        Button btn2 = this.findViewById(R.id.close_friend);
        TextView view = this.findViewById(R.id.result_user);

        Intent intent = getIntent();
        String message = intent.getStringExtra(SearchActivity.extraMessage);
        view.setText(message);
        Log.i(TAG, "onCreate: " + message);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
                setTitle("Added as Friend");
                Log.i("widgetDemo", "Added as Friend");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
                setTitle("Added as Close Friend");
                Log.i("widgetDemo", "Added as Close Friend");
            }
        });
    }
}
