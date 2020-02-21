package com.example.tryproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<PDItem> mData = null;
    private Context mContext;
    private PDItemAdapter mAdapter = null;
    private ListView list_pdItem;
    private Button addButton;
    private Button filterButton;
    private Button exportrButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        list_pdItem = (ListView) findViewById(R.id.list_pditem);
        mData = (ArrayList<PDItem>) getIntent().getSerializableExtra("mData");
//        for(int i=0;i<25;i++) {
//            PDItem d = new PDItem("电脑"+i, "房间"+i);
//            if(i<3)
//                d.setStatus(true);
//            mData.add(d);
//        }
        mAdapter = new PDItemAdapter(mData,mContext);
        list_pdItem.setAdapter(mAdapter);
        filterButton = findViewById(R.id.buttonfilter);
        addButton = findViewById(R.id.buttonscan);
        addButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ScanActivity.class );
                MainActivity.this.startActivity(i);
            }
        });

        filterButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FilterActivity.class );
                MainActivity.this.startActivity(i);
            }
        });
        exportrButton = findViewById(R.id.buttonexport);

    }

}
