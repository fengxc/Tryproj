package com.nfschina.pdScan;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PditemInfoFragment extends Fragment {

    public PDItemInfoFragmentListener mListener;

    public static interface PDItemInfoFragmentListener{

        //更新数据
        void updatePDItem(PDItem cuerrentPDItem);
    }

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
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mListener = (PDItemInfoFragmentListener) activity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (PDItemInfoFragmentListener) context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.pditem_info_fragment, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton b=getView().findViewById(R.id.conflictLogButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogInput();
            }
        });

    }

    public void showDialogInput(){
        final EditText edit = new EditText(this.getContext());

        AlertDialog.Builder editDialog = new AlertDialog.Builder(this.getContext());
        editDialog.setTitle("异常备注");
        editDialog.setIcon(R.mipmap.ic_launcher_round);

        //设置dialog布局
        editDialog.setView(edit);
        String conflictLog="";
        if(cuerrentPDItem.getConflictLog()!=null)
            conflictLog = cuerrentPDItem.getConflictLog();
        edit.setText(conflictLog);
        //设置按钮
        editDialog.setPositiveButton("提交"
                , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                cuerrentPDItem.setConflictLog(edit.getText().toString());
                mListener.updatePDItem(cuerrentPDItem);
                dialog.dismiss();
            }
        });
        editDialog.setNegativeButton("取消"
                , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        editDialog.create().show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
