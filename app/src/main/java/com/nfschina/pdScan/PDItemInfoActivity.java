package com.nfschina.pdScan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.nfschina.pdScan.dao.PDDao;

public class PDItemInfoActivity extends AppCompatActivity implements PditemInfoFragment.PDItemInfoFragmentListener {

    PDDataSourceService.PDQueryBinder binder;
    private ServiceConnection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pditem_info);
        Toolbar toolbarS =
                (Toolbar) findViewById(R.id.toolbari);
        setSupportActionBar(toolbarS);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        Intent intentService = new Intent();
        intentService.setAction("com.nfschina.pdScan.PDDataSourceService_Action");
        intentService.setPackage("com.nfschina.pdScan");
        conn =  new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (PDDataSourceService.PDQueryBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intentService,conn, Service.BIND_AUTO_CREATE);

        PditemInfoFragment pditemInfoFragment = (PditemInfoFragment) getSupportFragmentManager().findFragmentById(R.id.infofragment);
        PDItem p= (PDItem) getIntent().getSerializableExtra("mData");
        pditemInfoFragment.setCuerrentPDItem(p);
        pditemInfoFragment.updateUI();
    }

    @Override
    public void updatePDItem(PDItem cuerrentPDItem) {
//        Toast.makeText(this,
//                cuerrentPDItem.getConflictLog().trim(),Toast.LENGTH_SHORT).show();
        binder.updateConflictLog(cuerrentPDItem);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
