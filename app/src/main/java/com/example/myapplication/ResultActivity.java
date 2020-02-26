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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ResultActivity extends AppCompatActivity{
    private static final String TAG = "secondActivity";
    private String cur_user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private DocumentReference mDocRef = db.document("contact/" + cur_user + "/list" + message);
    //private CollectionReference mColRef = db.collection("user/");
    //private CollectionReference mColRef_add = db.collection("contact/" + cur_user + "/list");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Button btn1 = this.findViewById(R.id.friend);
        Button btn2 = this.findViewById(R.id.close_friend);
        TextView view = this.findViewById(R.id.result_user);

        final Map<String, Object> friend = new HashMap<>();
        friend.put("close", true);
        friend.put("money", 0);


        Intent intent = getIntent();
        final String message = intent.getStringExtra(SearchActivity.extraMessage);
        cur_user = getIntent().getExtras().getString("username");

        view.setText(message);
        Log.i(TAG, "onCreate: " + message);

        final CollectionReference mColRef_add = db.collection("contact/" + cur_user + "/list");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
                setTitle("Added as Friend");
                Log.i("widgetDemo", "Added as Friend");
                mColRef_add
                        .add(friend)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });

        final DocumentReference mDocRef = db.document("contact/" + cur_user + "/list/" + message);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
                setTitle("Added as Close Friend");
                Log.i("widgetDemo", "Added as Close Friend");
                mDocRef.update("close", true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });

    }

}
