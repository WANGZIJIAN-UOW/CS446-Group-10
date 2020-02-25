package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    public void setContentView(View view) {
        getWindow().setContentView(view);
    }
    private static final String TAG = "SearchActivity";

    private List<String> data(){
        List<String> data = new ArrayList<String>();
        data.add("Helen");
        data.add("Zijian");
        data.add("Shanglin");
        data.add("Shuangyou");
        data.add("Helen2");
        data.add("Helen3");
        return data;
    }

    //String[] mStrs = {"Helen", "Helen2", "Helen3", "Helen4"};
    private SearchView mSearchView;
    private ListView mListView;
    private Button button;
    public static final String extraMessage = "com.example.android.twoactivities.extra.MESSAGE";
    // private CollectionReference cities = db.collection("cities");
    //private DocumentReference docRef = db.collection("cities").document("SF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchView = (SearchView) findViewById(R.id.searchView2);

        mListView = (ListView) findViewById(R.id.mylist);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data());
        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);
        button = (Button) findViewById(R.id.search);


        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                Log.i(TAG, "onClick: " + mSearchView.getQuery().toString());
                intent.putExtra(extraMessage, mSearchView.getQuery().toString());
                startActivity(intent);
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }

        });

    }

}
