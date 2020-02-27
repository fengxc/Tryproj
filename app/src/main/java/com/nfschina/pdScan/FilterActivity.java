package com.nfschina.pdScan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.nfschina.pdScan.dao.PDDto;

import java.util.Calendar;

public class FilterActivity extends AppCompatActivity {
    EditText dateStart;
    EditText dateEnd;
    EditText snfield;
    EditText typefield;
    EditText markfield;
    EditText userfield;
    EditText locatefield;
    private PDDto dto;
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
        dateStart = findViewById(R.id.datePickerStart);
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



                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("dto", newDto);
                i.putExtras(bundle);
                setResult(RESULT_OK,i); //  向上一个活动返回数据
                finish();
            }
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        Calendar c = Calendar.getInstance();
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