package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    //    public static final String EMAIL_KEY = "email";
//    public static final String PWD_KEY = "pwd";
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("users/credentials");
    private Button signUpButton;
    private Button signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpButton = (Button) findViewById(R.id.signUpActionButton);
        signInButton = (Button) findViewById(R.id.signIn);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignUp();
            }
        });
    }

    public void goSignUp(){
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }

    public void goSignIn(){
    }

}