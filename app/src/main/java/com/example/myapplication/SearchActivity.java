package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.content.Context;
import android.database.Cursor;
import android.text.TextWatcher;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    ListView search_user;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_user = (ListView) findViewById(R.id.search_user);

        ArrayList<String> arrayUser = new ArrayList<>();
        arrayUser.addAll(Arrays.asList(getResources().getStringArray(R.array.my_users)));

        adapter = new ArrayAdapter<String>(
                SearchActivity.this,
                android.R.layout.simple_list_item_1,
                arrayUser
        );

        search_user.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_user);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
