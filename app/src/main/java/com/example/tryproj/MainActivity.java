package com.example.tryproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
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
        exportrButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String filecontentText ="";
                for(int index=0;index<mData.size();index++){
                    filecontentText+=mData.get(index).getItemName()+","+mData.get(index).getLocateString()+","+mData.get(index).isStatus()+"\r\n";
                }
                String filename = "Qone/"+System.currentTimeMillis()+".txt";
                try {
                    //判断SDcard是否存在并且可读写
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        saveToSDCard(filename,filecontentText);
                        Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.sdcarderror, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void saveToSDCard(String filename, String content)throws Exception {
        /*
         * 保存文件到sd卡，sd卡用于保存大文件（视频，音乐，文档等）
         * 获取sd卡路径Environment.getExternalStorageDirectory()
         * android版本不同，sd卡的路径也不相同，所以这里不能写绝对路径
         * */
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        FileOutputStream outStream = new FileOutputStream(file);


        outStream.write(content.getBytes());
        outStream.close();
    }
}
