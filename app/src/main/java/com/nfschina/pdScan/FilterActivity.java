package com.nfschina.pdScan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.nfschina.pdScan.dao.PDDto;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FilterActivity extends AppCompatActivity {
    Button startDel;
    Button endDel;
    Button snDel;
    Button typeDel;
    Button markDel;
    Button userDel;
    Button locateDel;
    EditText dateStart;
    EditText dateEnd;
    EditText snfield;
    EditText typefield;
    EditText markfield;
    EditText userfield;
    EditText locatefield;
    private PDDto dto;
    Button clear;
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.settings, new SettingsFragment())
//                .commit();
        ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
        //}
        PDDto returnData = (PDDto) getIntent().getSerializableExtra("dto");
        dto = returnData;

        snfield = findViewById(R.id.fieldsn);
        if(dto.getSn()!=null)
            snfield.setText(dto.getSn().substring(1,dto.getSn().length()-1));
        typefield = findViewById(R.id.fieldtype);
        if(dto.getType()!=null)
            typefield.setText(dto.getType().substring(1,dto.getType().length()-1));
        markfield = findViewById(R.id.fieldmark);
        if(dto.getMark()!=null)
            markfield.setText(dto.getMark().substring(1,dto.getMark().length()-1));
        userfield = findViewById(R.id.fielduser);
        if(dto.getUser()!=null)
            userfield.setText(dto.getUser().substring(1,dto.getUser().length()-1));
        locatefield = findViewById(R.id.fieldlocate);
        if(dto.getLocate()!=null)
            locatefield.setText(dto.getLocate().substring(1,dto.getLocate().length()-1));


        ok = findViewById(R.id.buttonapplysearch);

        DateFormat fmt =new SimpleDateFormat("yyyy/MM/dd");

        dateStart = findViewById(R.id.datePickerStart);
        if(dto.getPurchaseTimeStart()!=null){
            dateStart.setText(fmt.format(dto.getPurchaseTimeStart()));
        }
        dateStart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePickerDialog(dateStart);
                }
            }
        });
        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dateStart);

            }
        });
        dateEnd = findViewById(R.id.datePickerEnd);
        if(dto.getPurchaseTimeEnd()!=null){
            Date dend = dto.getPurchaseTimeEnd();
            Calendar c = Calendar.getInstance();
            c.setTime(new java.util.Date(dend.getTime()));
            c.add(Calendar.DATE, -1);
            dateEnd.setText(fmt.format(c.getTime().getTime()));
        }
        dateEnd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePickerDialog(dateEnd);
                }
            }
        });
        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dateEnd);

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PDDto newDto = new PDDto();
                String sn = snfield.getText().toString();
                if(sn!=null&&sn.length()>0){
                    newDto.setSn("%"+sn+"%");
                }
                String type = typefield.getText().toString();
                if(type!=null&&type.length()>0){
                    newDto.setType("%"+type+"%");
                }
                String mark = markfield.getText().toString();
                if(mark!=null&&mark.length()>0){
                    newDto.setMark("%"+mark+"%");
                }
                String user = userfield.getText().toString();
                if(user!=null&&user.length()>0){
                    newDto.setUser("%"+user+"%");
                }
                String locate = locatefield.getText().toString();
                if(locate!=null&&locate.length()>0){
                    newDto.setLocate("%"+locate+"%");
                }

                String start = dateStart.getText().toString();
                if(start!=null&&start.length()>0){
                    DateFormat fmt =new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        Date dstart = new Date(fmt.parse(start).getTime());
                        newDto.setPurchaseTimeStart(dstart);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                String end = dateEnd.getText().toString();
                if(end!=null&&end.length()>0){
                    DateFormat fmt =new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        Calendar c = Calendar.getInstance();
                        c.setTime(fmt.parse(end));
                        c.add(Calendar.DATE, 1);
                        Date dend = new Date(c.getTime().getTime());
                        newDto.setPurchaseTimeEnd(dend);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("dto", newDto);
                i.putExtras(bundle);
                setResult(RESULT_OK,i); //  向上一个活动返回数据
                finish();
            }
        });



        startDel= findViewById(R.id.dateStartDel);
        bindDelButton(dateStart, startDel);
        endDel= findViewById(R.id.dateEndDel);
        bindDelButton(dateEnd, endDel);
        snDel= findViewById(R.id.snDel);
        bindDelButton(snfield, snDel);
        typeDel= findViewById(R.id.typeDel);
        bindDelButton(typefield, typeDel);
        markDel= findViewById(R.id.markDel);
        bindDelButton(markfield, markDel);
        userDel= findViewById(R.id.userDel);
        bindDelButton(userfield, userDel);
        locateDel= findViewById(R.id.locateDel);
        bindDelButton(locatefield, locateDel);
        clear = findViewById(R.id.buttonclear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateStart.setText("");
                dateEnd.setText("");
                snfield.setText("");
                typefield.setText("");
                markfield.setText("");
                userfield.setText("");
                locatefield.setText("");
            }
        });
    }

    private void bindDelButton(final EditText dateStart, final Button startDel) {
        dateStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    startDel.setVisibility(View.VISIBLE);
                }else{
                    startDel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(dateStart.getText().toString().length()>0)
            startDel.setVisibility(View.VISIBLE);
        else
            startDel.setVisibility(View.GONE);
        startDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateStart.setText("");
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        DateFormat fmt =new SimpleDateFormat("yyyy/MM/dd");
            Calendar c = Calendar.getInstance();
            if(editText.getText()!=null&&editText.getText().toString().length()>0) {
                try {
                    c.setTime(fmt.parse(editText.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year+"/"+(monthOfYear+1)+"/"+dayOfMonth);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}