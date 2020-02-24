package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getName();
    public static final String EMAIL_KEY = "email";
    public static final String PWD_KEY = "pwd";
   // public static final String PWD_KEY = "UserName";
    //private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("users/credentials");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Button backButton = (Button) findViewById(R.id.backButton1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
            }
        });
    }

    public void signUp(View view) {
        EditText emailView = (EditText) findViewById(R.id.emailAddr1);
        EditText pwdView = (EditText) findViewById(R.id.password);
        EditText unameView = (EditText) findViewById(R.id.userName1);
        String email = emailView.getText().toString();
        String pwd = pwdView.getText().toString();
        String username = unameView.getText().toString();
        if (email.isEmpty() || pwd.isEmpty() || username.isEmpty()) return;
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(EMAIL_KEY, email);
        dataToSave.put(PWD_KEY, pwd);
        DocumentReference mDocRef = FirebaseFirestore.getInstance().document("users/credentials");
        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document has been saved");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Document was not saved");
            }
        });
    }
    public void goMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}