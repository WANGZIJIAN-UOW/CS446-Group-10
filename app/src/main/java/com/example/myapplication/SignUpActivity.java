package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getName();
    public static final String EMAIL_KEY = "email";
    public static final String PWD_KEY = "pwd";
    public static final String USERNAME = "username";
    FirebaseAuth mFirebaseAuth;
   // public static final String PWD_KEY = "UserName";
    private CollectionReference mColRef = FirebaseFirestore.getInstance().collection("users");
    private Boolean f = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mFirebaseAuth = FirebaseAuth.getInstance();
        Button backButton = (Button) findViewById(R.id.backButton1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
            }
        });
        setTitle("Sign Up");
    }

    public void signUp(View view) {
        EditText emailView = (EditText) findViewById(R.id.emailAddr1);
        EditText pwdView = (EditText) findViewById(R.id.loginPassword);
        EditText unameView = (EditText) findViewById(R.id.loginUserName);
        final String email = emailView.getText().toString();
        final String pwd = pwdView.getText().toString();
        final String username = unameView.getText().toString();
        if (email.isEmpty() || pwd.isEmpty() || username.isEmpty()) {
            Toast.makeText(this,"Some fields are empty!",Toast.LENGTH_SHORT);
            return;
        }
        // check username exist or not
        final DocumentReference docRef = mColRef.document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(SignUpActivity.this, "The username already exists" ,Toast.LENGTH_SHORT);
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "Username not used");
                        f = true;
                        Log.d(TAG, "Username and email address updated");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                if(f){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Sign up not successful, please try again", Toast.LENGTH_SHORT);
                            }
                            else{
                                // also set user with email and username
                                Map<String, Object> dataToSave = new HashMap<String, Object>();
                                dataToSave.put(EMAIL_KEY, email);
                                dataToSave.put(USERNAME, username);

                                docRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            }
                        }
                    });
                    Map<String, Object> data = new HashMap<>();
                    data.put("close", true);
                    data.put("money", 0);
                    FirebaseFirestore.getInstance().collection("contact").document(email)
                            .collection("list").document(email).set(data);
                }

            }
        });


    }


    public void goMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}