package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullBalance extends AppCompatActivity {

    public static class Contact {
        public String contact;
        public boolean close;
        public double money;
        public double wallet = 0.0;
        public Contact() { }
    }

    public static class wallet {
        public String contact;
        public double amount;
        public wallet() { }
    }

    private static final String TAG = SignUpActivity.class.getName();
    private Button back;
    private Button manualSettle;
    private ListView mListView;
    private String username;
    private String status;
    private CollectionReference mDocRef;
    private ArrayList<Contact> contacts = new ArrayList<Contact>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_balance);

        username = getIntent().getExtras().getString("email");
        status = getIntent().getExtras().getString("status");
        mDocRef = db.collection("contact/" + username + "/list");
        mListView = (ListView) findViewById(R.id.mylist);
        manualSettle = (Button)findViewById(R.id.manualSettle);

        fillData();

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
                                    contacts.add(contact);
                                } else if (status.equals("Outstanding") && contact.money < 0.0) {
                                    contacts.add(contact);
                                    manualSettle.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                    setList();
                    setWallets();
                }
            }
        });
    }

    public void setList() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < contacts.size(); i++) {
            if (status.equals("Owed")) {
                String balance = contacts.get(i).contact + ": $" + contacts.get(i).money;
                data.add(balance);
            } else if (status.equals("Outstanding")) {
                String balance = contacts.get(i).contact + ": $" + (contacts.get(i).money * -1);
                data.add(balance);
            }
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);
    }

    public ArrayAdapter getDropDown() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < contacts.size(); i++) {
            data.add(contacts.get(i).contact);
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, data);
        return adapter;
    }

    public void openBalance(){
        Intent intent = new Intent(this, ShowBalance.class);
        intent.putExtra("email", username);
        startActivity(intent);
    }

    public void setWallets() {
        db.collection("wallet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            wallet wallet = document.toObject(wallet.class);

                            for (int i = 0; i < contacts.size(); i++) {
                                if (document.getId().equals(contacts.get(i).contact)) {
                                    contacts.get(i).wallet = wallet.amount;
                                }
                            }
                        }
                    }
                    manualSettleUser();
                }
            }
        });
    }

    public void manualSettleUser() {
        manualSettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FullBalance.this);
                builder.setTitle("Select User for Manual Debt Settlement");

                ArrayAdapter<String> dataAdapter = getDropDown();
                builder.setAdapter(dataAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manualSettleAmount(which);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void manualSettleAmount(final int i) {
        final EditText amount = new EditText(this);
        amount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

        AlertDialog alertDialog = new AlertDialog.Builder(FullBalance.this).create();
        alertDialog.setTitle("Confirmation Screen");
        alertDialog.setMessage("Enter amount for " + contacts.get(i).contact);
        alertDialog.setView(amount);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateDebtWrapper(i, Double.parseDouble(amount.getText().toString()));
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

    public Map<String, Object> updateDebt(boolean close, double money) {
        Map<String, Object> dataToUpdate = new HashMap<String, Object>();
        dataToUpdate.put("close", true);
        dataToUpdate.put("money", money);
        return dataToUpdate;
    }

    public void updateDebtWrapper(final int i, double amount) {
        if (amount >= (contacts.get(i).money *  -1)) {
            db.collection("contact").document(username)
                    .collection("list").document(contacts.get(i).contact).update(updateDebt(true,  0));
            db.collection("contact").document(contacts.get(i).contact)
                    .collection("list").document(username).update(updateDebt(true, 0));

        } else {
            db.collection("contact").document(username)
                    .collection("list").document(contacts.get(i).contact).update(updateDebt(true,  contacts.get(i).money + amount));
            db.collection("contact").document(contacts.get(i).contact)
                    .collection("list").document(username).update(updateDebt(true, (contacts.get(i).money + amount) * -1));
        }
        Intent intent = new Intent(this, ShowBalance.class);
        intent.putExtra("email", username);
        startActivity(intent);

    }
}
