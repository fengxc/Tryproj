package com.nfschina.pdScan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
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
        //buttonQuery = findViewById(R.id.buttonquery);
        editText = findViewById(R.id.fileActEditText);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text ="test";
                text = editText.getText().toString();
                Intent intent = new Intent(); //  该Intent仅用于传递数据，没有指定任何"意图"
                intent.putExtra("scanResult",text); // 把要传递的数据放到Intent中
                setResult(RESULT_OK,intent); //  向上一个活动返回数据
                finish();  //  销毁当前活动
            }
        });
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
                .setMessage("未保存，要保存吗？")
                .setNegativeButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(); //  该Intent仅用于传递数据，没有指定任何"意图"
                        String text ="test";
                        text = editText.getText().toString();
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


}
