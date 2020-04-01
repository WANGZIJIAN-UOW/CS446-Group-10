package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactActivityNew extends AppCompatActivity{
    private FirebaseFirestore db;
    private ListView mContactListView;
    //private ListView mIDs;
    private ContactAdapter mContactAdapter;
    private ArrayList<ContactItem> mContactList;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mContactListView = (ListView) findViewById(R.id.attributes);

        //mContactListView = (ListView) findViewById(R.id.contact_list);
        //mContactListView = (ListView) findViewById(R.id.contact_list);
        db = FirebaseFirestore.getInstance();
        //mContactList = new ArrayList<ContactItem>();

       //final ContactAdapter mContactAdapter = new ContactAdapter(this, mContactList);

        //mContactListView.setAdapter(mContactAdapter);

        final String cur_user2 = getIntent().getExtras().getString("email");


        db.collection("contact/" + cur_user2 + "/list/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mContactList = new ArrayList<>();
                //List<String> mFriends = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()) {
                       ContactItem con = document.toObject(ContactItem.class);
                       mContactList.add(con);
                       String id = document.getId();
                       //mFriends.add(id);
                    }
                    //mIDs = (ListView) findViewById(R.id.contact_list);
                    //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ContactActivityNew.this, android.R.layout.select_dialog_singlechoice, mFriends);
                    mContactAdapter = new ContactAdapter(ContactActivityNew.this, mContactList);
                    //mContactAdapter.notifyDataSetChanged();
                    mContactListView.setAdapter(mContactAdapter);
                    //mIDs.setAdapter(arrayAdapter);
                } else {
                    Log.d("MissionActivity", "Error getting documents: ", task.getException());
                }
            }
        });

        mContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ContactActivityNew.this, "selected the friend", Toast.LENGTH_LONG).show();
                //Toast.makeText(ContactActivityNew.this,"你单击的是第"+(position+1)+"条数据",Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(ContactActivityNew.this,ContactResultActivity.class);
                intent.putExtra("friendname",mContactList.get(position+1).getName());
                startActivity(intent);
                finish();*/
            }
        });
    }
}