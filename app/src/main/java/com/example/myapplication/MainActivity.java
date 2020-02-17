package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    public static final String EMAIL_KEY = "email";
    public static final String PWD_KEY = "pwd";
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("users/credentials");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signUp(View view){
        EditText emailView = (EditText) findViewById(R.id.emailAddr1);
        EditText pwdView = (EditText) findViewById(R.id.password);
        String email = emailView.getText().toString();
        String pwd = pwdView.getText().toString();
        if(email.isEmpty() || pwd.isEmpty()) return;
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(EMAIL_KEY, email);
        dataToSave.put(PWD_KEY, pwd);

        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Document has been saved");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Document was not saved");
            }
        });
    }
}
