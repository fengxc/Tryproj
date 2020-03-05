package com.nfschina.pdScan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class PDItemInfoActivity extends AppCompatActivity {

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

        PditemInfoFragment pditemInfoFragment = (PditemInfoFragment) getSupportFragmentManager().findFragmentById(R.id.infofragment);
        PDItem p= (PDItem) getIntent().getSerializableExtra("mData");
        pditemInfoFragment.setCuerrentPDItem(p);
        pditemInfoFragment.updateUI();
    }
}
