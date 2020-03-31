package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryLoans extends AppCompatActivity {

    public static class Loans {
        public String Amount;
        public String Creditor;
        public String Debtor;
        public Loans() { }
    }

    private static final String TAG = SignUpActivity.class.getName();
    private Button back;
    private ListView mListView;
    private String username;
    private CollectionReference mDocRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_loans);

        username = getIntent().getExtras().getString("email");
        mDocRef = db.collection("Loans");
        mListView = (ListView) findViewById(R.id.mylist);

        getLoans();

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBalance();
            }
        });

    }

    public void getLoans() {
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            Loans loan = document.toObject(Loans.class);
                            if (loan.Debtor.equals(username)) {
                                String text = "Lent " +  loan.Creditor + " " + loan.Amount;
                                data.add(text);
                            } else if(loan.Creditor.equals(username)) {
                                String text = "Borrowed " + loan.Amount +  " from " +  loan.Debtor;
                                data.add(text);
                            }
                        }
                    }
                    loansToList();
                }
            }
        });
    }

    public void openBalance(){
        Intent intent = new Intent(this, ShowBalance.class);
        intent.putExtra("email", username);
        startActivity(intent);
    }

    public void loansToList() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);
    }
}
