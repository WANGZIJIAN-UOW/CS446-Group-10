package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultActivity extends AppCompatActivity{
    private static final String TAG = "secondActivity";

    //DatabaseReference mDatabase;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mDocRef = db.document("contact/shanglin1/list/shanglin2");


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
                mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

    }

}
