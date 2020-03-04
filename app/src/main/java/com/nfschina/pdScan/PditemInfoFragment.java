package com.nfschina.pdScan;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;


import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PditemInfoFragment extends Fragment {

    private PDItem cuerrentPDItem;

    public PDItem getCuerrentPDItem() {
        return cuerrentPDItem;
    }

    public void setCuerrentPDItem(PDItem cuerrentPDItem) {
        this.cuerrentPDItem = cuerrentPDItem;
    }

    public void updateUI() {
        if (cuerrentPDItem !=null){
            TextView sn = getView().findViewById(R.id.infoSnContent);
            sn.setText(cuerrentPDItem.getSn());
            TextView type = getView().findViewById(R.id.infoTypeContent);
            type.setText(cuerrentPDItem.getTypeString());
            TextView mark = getView().findViewById(R.id.infoMarkContent);
            mark.setText(cuerrentPDItem.getMarkString());
        }
    }


    public static PditemInfoFragment newInstance() {
        return new PditemInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.pditem_info_fragment, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
