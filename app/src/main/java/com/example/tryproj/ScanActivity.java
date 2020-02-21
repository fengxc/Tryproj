package com.example.tryproj;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends AppCompatActivity {
    private EditText editText;
    private Button buttonSave;
    private File file;
    private List<String> curNums = new ArrayList<String>();
    private String sep = "\r\n";

    private boolean hasChanged = false;
    private Button buttonQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        buttonSave = findViewById(R.id.buttonsave);
        buttonQuery = findViewById(R.id.buttonquery);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && hasChanged){
            createQuitAlert();
            return false;
        }
        return super.onKeyUp(keyCode, event);

    }

    private void createQuitAlert(){
        new AlertDialog.Builder(ScanActivity.this)
                .setTitle("退出")
                .setMessage("要保存吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //writeFile(file);
                        Toast.makeText(getApplicationContext(), "文件已保存", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .create().show();
    }


}
