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

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    String[] mStrs = {"Helen", "Helen2", "Helen3", "Helen4"};
    SearchView mSearchView;
    ListView mListView;
    private Button button;
    public static final String extraMessage = "com.example.android.twoactivities.extra.MESSAGE";
    // private CollectionReference cities = db.collection("cities");
    //private DocumentReference docRef = db.collection("cities").document("SF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchView = (SearchView) findViewById(R.id.searchView);

        mListView = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs);
        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);
        button = (Button) findViewById(R.id.search);


        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示方式声明Intent，直接启动SecondActivity
                Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                Log.i(TAG, "onClick: " + mSearchView.getQuery().toString());
                intent.putExtra(extraMessage, mSearchView.getQuery().toString());
                startActivity(intent);
            }
        });


        // 设置搜索文本监听
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
