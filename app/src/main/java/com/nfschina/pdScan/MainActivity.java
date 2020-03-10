package com.nfschina.pdScan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private List<PDItem> mData = null;
    private Context mContext;
    private PDItemAdapter mAdapter = null;
    private ListView list_pdItem;
    private FloatingActionButton addButton;
    private String sep = "\n";
    private boolean auth = false;
    private String pw;
    private int mode = 0;//0全部 1未扫 2已扫
    private Toolbar toolbar;
    PDDataSourceService.PDQueryBinder binder;
    private ServiceConnection conn =  new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (PDDataSourceService.PDQueryBinder) service;
            //updateUIStatus();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    IntentFilter updateFilter = new IntentFilter("com.nfschina.pdScan.PDDataSourceService_ListUpdate");
    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            updateUIStatus();
        }
    };
    private NavigationView navigationView;
    private TabLayout tabs;
    private SharedHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        drawerLayout = findViewById(R.id.drawerlayout);
        Intent intentService = new Intent();
        intentService.setAction("com.nfschina.pdScan.PDDataSourceService_Action");
        intentService.setPackage("com.nfschina.pdScan");
        //startService(intentService);


        bindService(intentService,conn, Service.BIND_AUTO_CREATE);
        registerReceiver(updateReceiver, updateFilter);


        mData = new ArrayList<PDItem>();
        list_pdItem = findViewById(R.id.list_pditem);
        list_pdItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //parent 代表listView View 代表 被点击的列表项 position 代表第几个 id 代表列表编号
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, mData.get(position).getSn(), Toast.LENGTH_LONG).show();
                Intent i2 = new Intent(MainActivity.this, PDItemInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("mData", mData.get(position));
                //bundle.putString("mData", mData.get(position).getSn());
                i2.putExtras(bundle);
                MainActivity.this.startActivity(i2);
            }
        });
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.alldept);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT,true);
            }
        });
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT,true);
            }
        });
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(getApplicationContext(), item.getOrder()+"", Toast.LENGTH_SHORT).show();
                binder.changeDept(item.getOrder());
                updateUIStatus();
                String deptName = (String) binder.getMap().get(item.getOrder());
                toolbar.setTitle(deptName);

                drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });

        tabs = findViewById(R.id.statusTab);
        helper = new SharedHelper(getApplicationContext());
        if(helper.contains("password")){
            pw=(String) helper.get("password", "");
        }else {
            auth = true;
            pw="";
        }

        if(helper.contains("viewStatus")){
            Integer viewStatus = (Integer) helper.get("viewStatus",new Integer(0));
            tabs.getTabAt(viewStatus).select();
        }
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(getApplicationContext(), tab.getPosition()+"", Toast.LENGTH_SHORT).show();
                binder.changeStatus(tab.getPosition());
                updateUIStatus();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //mData = (ArrayList<PDItem>) getIntent().getSerializableExtra("mData");

        //dao.insertPDItems(mData);

//        for(int i=0;i<25;i++) {
//            PDItem d = new PDItem("电脑"+i, "房间"+i);
//            if(i<3)
//                d.setStatus(true);
//            mData.add(d);
//        }

        //filterButton = findViewById(R.id.buttonfilter);
        addButton = findViewById(R.id.scanButton);
        addButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ScanActivity.class);
                MainActivity.this.startActivityForResult(i, 1);
            }
        });


//        filterButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        exportButton = findViewById(R.id.buttonexport);
//        exportButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//            }
//
//
//        });
//        importButton = findViewById(R.id.buttonimport);
//        importButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!auth)
            showPasswordValidationInput(pw);

    }

    private void updateUIStatus() {
        mData = binder.getCurrentResult();
        if(mAdapter==null)
            mAdapter = new PDItemAdapter(mData, mContext);
        else
            mAdapter.setItemList(mData);
        list_pdItem.setAdapter(mAdapter);
        navigationView.getMenu().clear();
        TextView head = findViewById(R.id.headtitle);
        head.setText(binder.getExcelName());
        TextView sub = findViewById(R.id.headsubtitle);
        sub.setText(binder.getExcelDate());
        Map map = binder.getMap();
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext()) {

            Map.Entry entry = (Map.Entry) entries.next();

            Integer key = (Integer)entry.getKey();

            String value = (String) entry.getValue();



            navigationView.getMenu().add(1, key, key, value);
        }
        int status = binder.getViewStatus();
        String nowDeptName = (String) map.get(binder.getDeptIndex());
        toolbar.setTitle(nowDeptName);
        tabs.getTabAt(0).setText("全部"+"("+binder.getNumofAll()+")");
        tabs.getTabAt(1).setText("已盘点"+"("+binder.getNumofDone()+")");
        tabs.getTabAt(2).setText("未盘点"+"("+binder.getNumofUnDone()+")");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: // 确定数据来源 SCAN
                if (resultCode == RESULT_OK) {
                    String returnData = data.getStringExtra("scanResult");
                    Toast.makeText(getApplicationContext(), returnData, Toast.LENGTH_SHORT).show();
                    String[] result = returnData.split(sep);
                    for (String s : result) {
                        binder.checkPDItem(s);
                    }
                }
                break;
            case 2://查询
                if (resultCode == RESULT_OK) {
                    //binder.refresh();
                    updateUIStatus();
                }
                break;
            case 3://导入
                if (resultCode == RESULT_OK) {
                    ArrayList<PDItem> newData = (ArrayList<PDItem>) data.getSerializableExtra("mData");
                    String name =data.getStringExtra("name");
                    String date =data.getStringExtra("date");
                    binder.importPDList(newData,name,date);
                }
                break;
            default:
                break;
        }
    }

    public void saveToSDCard(String filename, String content) throws Exception {
        /*
         * 保存文件到sd卡，sd卡用于保存大文件（视频，音乐，文档等）
         * 获取sd卡路径Environment.getExternalStorageDirectory()
         * android版本不同，sd卡的路径也不相同，所以这里不能写绝对路径
         * */
        String qoneFolderN = "Qone";
        File folder = new File(Environment.getExternalStorageDirectory(), qoneFolderN);
        if(!folder.exists()){
            folder.mkdir();
        }

        File file = new File(Environment.getExternalStorageDirectory(), filename);
//        if (file==null){
//            String sdpath = Environment.getExternalStorageDirectory()
//                    .getAbsolutePath();
//            file = new File(File.separator + "mnt" + File.separator + "sdcard" + sdpath + File.separator, filename);
//        }
        FileOutputStream outStream = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(outStream, "unicode");
        BufferedWriter bw = new BufferedWriter(osw);


        bw.write(content);
        bw.close();
    }

    private void expotResult() {
        if (!getPermission())
            return;
        final EditText edit = new EditText(mContext);
        edit.setInputType(InputType.TYPE_CLASS_TEXT);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(mContext);
        editDialog.setTitle("请输入导出文件名");
        editDialog.setIcon(R.mipmap.ic_launcher_round);
        //设置dialog布局
        editDialog.setView(edit);

        //设置按钮
        editDialog.setPositiveButton("提交"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String filenameStr = edit.getText().toString();
                        final String filename = "Qone/" + filenameStr + ".txt";

                        new Thread(new Runnable() {
                            public void run() {

                                String filecontentText = "";
                                PDItem[] allPDItem= binder.getPdItemsForExport();

                                for (int index = 0; index < allPDItem.length; index++) {
                                        filecontentText += getString(R.string.scan_Pre)+allPDItem[index].getSn()  + "\r\n";
                                }
                                try {
                                    //判断SDcard是否存在并且可读写
                                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                        saveToSDCard(filename, filecontentText);
                                        Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), R.string.sdcarderror, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

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

    private void expotResultLog() {
        if (!getPermission())
            return;
        final EditText edit = new EditText(mContext);
        edit.setInputType(InputType.TYPE_CLASS_TEXT);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(mContext);
        editDialog.setTitle("请输入导出文件名");
        editDialog.setIcon(R.mipmap.ic_launcher_round);
        //设置dialog布局
        editDialog.setView(edit);

        //设置按钮
        editDialog.setPositiveButton("提交"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            String filenameStr = edit.getText().toString();
                            final String filename = "Qone/" + filenameStr + ".csv";
                            new Thread(new Runnable() {
                                public void run() {
                                    String filecontentText = "";
                                    PDLog[] allPDLogs= binder.getPdLogsForExport();
                                    for (int index = 0; index < allPDLogs.length; index++) {
                                        filecontentText += index +1+ "," + allPDLogs[index].getScanDate().toString() + "," + allPDLogs[index].getSn() + "," + allPDLogs[index].getConflictLog()  + "\r\n";
                                    }

                                    try {
                                        //判断SDcard是否存在并且可读写
                                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                            saveToSDCard(filename, filecontentText);
                                            Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), R.string.sdcarderror, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.importExcel:
                Intent i = new Intent(MainActivity.this, SelectFileActivity.class);
                MainActivity.this.startActivityForResult(i, 3);
                return true;
            case R.id.exportResult:
                expotResult();
                return true;
            case R.id.exportConflictLog:
                expotResultLog();
                return true;
            case R.id.searchMenu:
                Intent i2 = new Intent(MainActivity.this, FilterActivity.class);
                MainActivity.this.startActivityForResult(i2, 2);
                return true;
            case R.id.changepw:
                MainActivity.this.showPasswordChangeInput();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void showPasswordValidationInput(final String pw){
        final EditText edit = new EditText(mContext);
        edit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(mContext);
        editDialog.setTitle("请输入密码");
        editDialog.setIcon(R.mipmap.ic_launcher_round);
        editDialog.setCancelable(false);
        //设置dialog布局
        editDialog.setView(edit);

        //设置按钮
        editDialog.setPositiveButton("提交"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(edit.getText().toString().equals(pw)){
                            auth = true;
                            dialog.dismiss();
                        }else{
                            showPasswordValidationInput(pw);
                            dialog.dismiss();
                        }

                    }
                });
        editDialog.setNegativeButton("退出"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MainActivity.this.finish();
                    }
                });

        editDialog.create().show();
    }


    public void showPasswordChangeInput(){
        final EditText edit = new EditText(mContext);
        edit.setEms(12);
        final EditText edit2 = new EditText(mContext);
        edit2.setEms(12);
        final LinearLayout l = new LinearLayout(mContext);
        edit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edit2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(mContext);
        editDialog.setTitle("请输入新密码两次，留空视为取消密码");
        editDialog.setIcon(R.mipmap.ic_launcher_round);
        editDialog.setCancelable(false);
        //设置dialog布局
        l.addView(edit);
        l.addView(edit2);
        l.setOrientation(LinearLayout.VERTICAL);
        editDialog.setView(l);

        //设置按钮
        editDialog.setPositiveButton("提交",null);
//        editDialog.setPositiveButton("提交"
//                , new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        if(edit.getText().toString().equals(pw)){
////                            auth = true;
////                            dialog.dismiss();
////                        }else{
////                            showPasswordValidationInput(pw);
////                            dialog.dismiss();
////                        }
//
//                    }
//                });
        editDialog.setNegativeButton("取消"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog a = editDialog.create();
        a.show();
        a.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                        if(edit.getText().toString().equals(edit2.getText().toString())){
                            String newPw = edit.getText().toString();
                            if(newPw.length()>0)
                               helper.put("password",newPw);
                            else
                                helper.remove("password");
                            a.dismiss();
                        }else{
                            edit.setText("");
                            edit2.setText("");
                            a.setTitle("两次输入不一致，请重试");
                        }
            }
        });
    }

    private boolean getPermission() {
        //检查权限是否存在
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            //向用户申请授权
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);

        }

        return  (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        unregisterReceiver(updateReceiver);
    }
}