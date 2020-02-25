package com.nfschina.pdScan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nfschina.pdScan.dao.PDDao;
import com.nfschina.pdScan.dao.PDItemDataBase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ArrayList<PDItem> mData = null;
    private Context mContext;
    private PDItemAdapter mAdapter = null;
    private ListView list_pdItem;
    private Button addButton;
    private Button filterButton;
    private Button exportButton;
    private Button importButton;
    private String sep = "\n";
    private PDItemDataBase db;
    private PDDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        mData = new ArrayList<PDItem>();
        list_pdItem = (ListView) findViewById(R.id.list_pditem);
        //mData = (ArrayList<PDItem>) getIntent().getSerializableExtra("mData");
        db= Room.databaseBuilder(getApplicationContext(), PDItemDataBase.class, "pdDatabase").allowMainThreadQueries().build();
        dao = db.pdDao();
        //dao.insertPDItems(mData);
        PDItem[] now = dao.loadPDItems();
        Collections.addAll(mData,now);
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
                MainActivity.this.startActivityForResult(i,1);
            }
        });

        filterButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FilterActivity.class );
                MainActivity.this.startActivityForResult(i,2);
            }
        });
        exportButton = findViewById(R.id.buttonexport);
        exportButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String filecontentText ="";
                for(int index=0;index<mData.size();index++){
                    filecontentText+=mData.get(index).getSn()+","+mData.get(index).getLocateString()+","+mData.get(index).isStatus()+"\r\n";
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
        importButton = findViewById(R.id.buttonimport);
        importButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SelectFileActivity.class );
                MainActivity.this.startActivityForResult(i,3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: // 确定数据来源
                if (resultCode == RESULT_OK) {
                    String returnData = data.getStringExtra("scanResult");
                    Toast.makeText(getApplicationContext(), returnData, Toast.LENGTH_SHORT).show();
                    String[]result = returnData.split(sep);
                    for(String s:result) {
                        for (PDItem p : mData) {
                            if(s.indexOf(p.getSn())>0) {
                                p.setStatus(true);
                                dao.updatePDItem(p);
                            }
                        }
                    }
                    mAdapter = new PDItemAdapter(mData,mContext);
                    list_pdItem.setAdapter(mAdapter);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    ArrayList<PDItem> newData = (ArrayList<PDItem>) data.getSerializableExtra("mData");
                    PDItem[] oldData = dao.loadPDItems();
                    dao.deletePDItems(oldData);
                    mData.clear();
                    for(PDItem p:newData){
                        dao.insertPDItem(p);
                        mData.add(p);
                    }
                    mAdapter = new PDItemAdapter(mData,mContext);
                    list_pdItem.setAdapter(mAdapter);
                }
                break;
        }
    }

    public void saveToSDCard(String filename, String content)throws Exception {
        /*
         * 保存文件到sd卡，sd卡用于保存大文件（视频，音乐，文档等）
         * 获取sd卡路径Environment.getExternalStorageDirectory()
         * android版本不同，sd卡的路径也不相同，所以这里不能写绝对路径
         * */
        File file = new File(Environment.getExternalStorageDirectory(), filename);
//        if (file==null){
//            String sdpath = Environment.getExternalStorageDirectory()
//                    .getAbsolutePath();
//            file = new File(File.separator + "mnt" + File.separator + "sdcard" + sdpath + File.separator, filename);
//        }
        FileOutputStream outStream = new FileOutputStream(file);


        outStream.write(content.getBytes());
        outStream.close();
    }
}
