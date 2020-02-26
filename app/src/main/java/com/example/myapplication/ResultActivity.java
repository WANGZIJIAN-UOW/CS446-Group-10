package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
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

        CircularImageView c = (CircularImageView) findViewById(R.id.profileImage);
        c.setBorderWidth(10);
        final Map<String, Object> friend = new HashMap<>();
        friend.put("close", true);
        friend.put("money", 0);


        Intent intent = getIntent();
        final String message = intent.getStringExtra(SearchActivity.extraMessage);
        final String cur_user = getIntent().getExtras().getString("email");

        view.setText(message);
        Log.i(TAG, "onCreate: " + message);

        final CollectionReference mColRef_add = db.collection("contact/" + cur_user + "/list");
        final CollectionReference mColRef_add_reverse = db.collection("contact/" + message + "/list");

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
                Log.i("widgetDemo", "Added as Friend");
                mColRef_add.document("/"+message)
                        .set(friend)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ResultActivity.this, "You've added a friend", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Added friend success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                mColRef_add_reverse.document("/"+cur_user)
                        .set(friend)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Added friend success");
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
                Log.i("widgetDemo", "Changed to Close Friend");
                mDocRef.update("close", true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ResultActivity.this, "You've added a close friend", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Added a close friend");
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
