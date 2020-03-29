package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FullBalance extends AppCompatActivity {

    public static class Contact {
        public String contact;
        public boolean close;
        public double money;
        public Contact() { }
    }

    private static final String TAG = SignUpActivity.class.getName();
    private Button back;
    private Button manualSettle;
    private ListView mListView;
    private String username;
    private String status;
    private CollectionReference mDocRef;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_balance);

        username = getIntent().getExtras().getString("email");
        status = getIntent().getExtras().getString("status");
        mDocRef = db.collection("contact/" + username + "/list");
        mListView = (ListView) findViewById(R.id.mylist);

        fillData();

        /*manualSettle = (Button)findViewById(R.id.manualSettle);
        manualSettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(FullBalance.this).create();
                alertDialog.setTitle("Confirmation Screen");
                alertDialog.setMessage("Do you wish to settle all your debts?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });*/

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBalance();
            }
        });

    }

    private void fillData(){
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            Contact contact = document.toObject(Contact.class);
                            contact.contact = document.getId();
                            if (contact.money != 0.0) {
                                if (status.equals("Owed") && contact.money > 0.0) {
                                    String balance = contact.contact + ": $" + contact.money;
                                    data.add(balance);
                                } else if (status.equals("Outstanding") && contact.money < 0.0) {
                                    String balance = contact.contact + ": $" + (contact.money * -1);
                                    data.add(balance);
                                }
                            }
                        }
                    }
                    setList();
                }
            }
        });
    }

    public void setList() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);
    }

    public void openBalance(){
        Intent intent = new Intent(this, ShowBalance.class);
        intent.putExtra("email", username);
        startActivity(intent);
    }

}
