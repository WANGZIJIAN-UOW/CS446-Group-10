package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddLoanActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getName();
    private Button addLoan;
    private Button back;
    private String username;
    private ListView listView;
    private DatabaseReference mDatabase;
    private ArrayList<String> arrayList=new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_loan);

        username = getIntent().getExtras().getString("username");

        mDatabase = FirebaseDatabase.getInstance().getReference("Loans");
        listView = (ListView)findViewById(R.id.loanList);



        addLoan = (Button) findViewById(R.id.addLoan);
        addLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddLoan();
            }
        });

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBalance();
            }
        });

        db.collection("Loans")
                .whereEqualTo("Debtor", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Loans loan = document.toObject(Loans.class);
                                //arrayList.add(loan.toString());
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);










    }
    public void openAddLoan(){
        Intent intent = new Intent(this, AddLoan.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
    public void openBalance(){
        Intent intent = new Intent(this, ShowBalance.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}
