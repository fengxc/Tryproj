package com.nfschina.pdScan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends AppCompatActivity  implements PditemInfoFragment.PDItemInfoFragmentListener{
    private ServiceConnection conn =  new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (PDDataSourceService.PDQueryBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    PDDataSourceService.PDQueryBinder binder;
    IntentFilter updateFilter = new IntentFilter("com.nfschina.pdScan.PDDataSourceService_PDItemUpdate");
    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if(pditemInfoFragment!=null) {
                PDItem[] result = binder.getPdItemSNQueryResult();
                if (result.length > 0) {
                    if(pditemInfoFragment==null) {
                        pditemInfoFragment = new PditemInfoFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.scanLayout, pditemInfoFragment).commit();
                    binder.checkPDItem(result[0].getSn());
                    if(result[0].isStatus()){
                        Toast.makeText(ScanActivity.this,"该资产"+result[0].getSn()+"已盘点过",Toast.LENGTH_LONG).show();
                        Uri notifyUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone mRingtone = RingtoneManager.getRingtone(ScanActivity.this, notifyUri);
                        mRingtone.play();
                    }else {
                        result[0].setStatus(true);
                    }
                    String conflictLog = "";
                    if(binder.getDeptIndex()>0) {
                        if(binder.getMap().get(binder.getDeptIndex()).toString().equals(result[0].getDeptString())){
                            conflictLog = "";
                        }else{
                            conflictLog = "盘点范围为"+binder.getMap().get(binder.getDeptIndex()).toString()
                                    +"，资产所属部门为"+result[0].getDeptString()+"，两者不一致";
                            Toast.makeText(ScanActivity.this,conflictLog,Toast.LENGTH_LONG).show();
                            Uri notifyUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone mRingtone = RingtoneManager.getRingtone(ScanActivity.this, notifyUri);
                            mRingtone.play();
                        }
                        result[0].setConflictLog(conflictLog);

                    }
                    binder.updateConflictLog(result[0]);
                    pditemInfoFragment.setCuerrentPDItem(result[0]);
                    pditemInfoFragment.updateUI();
                }else{
                    NoDataFragment noDataFragment = new NoDataFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.scanLayout, noDataFragment).commit();
                }
            }
        }
    };




    private EditText editText;
    //private Button buttonSave;
    //private Button buttonScan;

    private List<String> curNums = new ArrayList<String>();
    private String sep = "\r\n";
    private boolean hasChanged = false;
    //private Button buttonQuery;
    PditemInfoFragment pditemInfoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Intent intentService = new Intent();
        intentService.setAction("com.nfschina.pdScan.PDDataSourceService_Action");
        intentService.setPackage("com.nfschina.pdScan");
        bindService(intentService,conn, Service.BIND_AUTO_CREATE);
        registerReceiver(updateReceiver, updateFilter);
        Toolbar toolbarS =
                (Toolbar) findViewById(R.id.toolbarS);
        setSupportActionBar(toolbarS);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        //buttonSave = findViewById(R.id.buttonsave);
        //buttonQuery = findViewById(R.id.buttonquery);
        editText = findViewById(R.id.fileActEditText);
        ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
        //}
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hasChanged=true;
                String str = editText.getText().toString();
                if(str.contains("\n")){
                    if(str.indexOf(":")>0) {
                        if(pditemInfoFragment==null) {
                            pditemInfoFragment = new PditemInfoFragment();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.scanLayout, pditemInfoFragment).commit();
                        str=str.substring(0,str.length()-1);
                        str=str.split(":")[1];
                        binder.queryPdItemBySN(str);
                    }else{
                        NoDataFragment noDataFragment = new NoDataFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.scanLayout, noDataFragment).commit();

                        str=str.substring(0,str.length()-1);
                        PDLog pdLog = new PDLog();
                        pdLog.setScanDate(new Date(System.currentTimeMillis()));
                        pdLog.setSn(str);
                        pdLog.setConflictLog(getString(R.string.pdlog_notfound));
                        binder.insertPDLog(pdLog);
                    }

                    editText.setText("");
                }
            }
        });
//        buttonSave.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String text = editText.getText().toString();
//                Intent intent = new Intent(); //  该Intent仅用于传递数据，没有指定任何"意图"
//                intent.putExtra("scanResult",text); // 把要传递的数据放到Intent中
//                setResult(RESULT_OK,intent); //  向上一个活动返回数据
//                finish();  //  销毁当前活动
//            }
//        });
//        buttonScan = findViewById(R.id.buttonScan);
//        buttonScan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String text = editText.getText().toString();
//                if(pditemInfoFragment==null) {
//                    pditemInfoFragment = new PditemInfoFragment();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.scanLayout, pditemInfoFragment).commit();
//                }
//                String pre = getString(R.string.scan_Pre);
//                if(text.length()> pre.length())
//                    binder.queryPdItemBySN(text.substring(pre.length()));
//                else{
//
//                }
//            }
//        });
    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event){
//        if (keyCode == KeyEvent.KEYCODE_BACK && hasChanged){
//            //createQuitAlert();
//            return false;
//        }
//        return super.onKeyUp(keyCode, event);
//
//    }

    private void createQuitAlert(){
        new AlertDialog.Builder(ScanActivity.this)
                .setTitle("退出")
                .setMessage("未保存，要保存吗？")
                .setNegativeButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(); //  该Intent仅用于传递数据，没有指定任何"意图"
                        String text = editText.getText().toString();
                        intent.putExtra("scanResult",text); // 把要传递的数据放到Intent中
                        setResult(RESULT_OK,intent); //  向上一个活动返回数据
                        finish();  //  销毁当前活动
                    }
                })
                .setPositiveButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //writeFile(file);
                        Toast.makeText(getApplicationContext(), "扫描内容已废弃", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        unregisterReceiver(updateReceiver);
    }

    @Override
    public void updatePDItem(PDItem cuerrentPDItem) {
//        Toast.makeText(this,
//                cuerrentPDItem.getConflictLog().trim(),Toast.LENGTH_SHORT).show();
        binder.updateConflictLog(cuerrentPDItem);
    }
}
