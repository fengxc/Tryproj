package com.nfschina.pdScan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SelectFileActivity extends AppCompatActivity {
    Button buttonSelectFile;
    private Context context;
    private ArrayList<PDItem> mData;
    private PDApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (PDApplication) getApplication();

        context = SelectFileActivity.this;
        setContentView(R.layout.activity_select_file);
        buttonSelectFile = findViewById(R.id.buttonselectfile);
        buttonSelectFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent i = new Intent(SelectFileActivity.this, MainActivity.class );
//                SelectFileActivity.this.startActivity(i);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            File sourceFile = null;
            String docId = null;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                docId = DocumentsContract.getDocumentId(uri);

                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    sourceFile = new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
                }
//            Toast.makeText(SelectFileActivity.this, "时间"+sourceFile.lastModified()+"...", Toast.LENGTH_SHORT).show();
            }
            Date lastModified = new Date();
            if (sourceFile!=null)
                lastModified = new Date(sourceFile.lastModified());
            final Date rDate = lastModified;
            String img_path = getFilePathForN(uri, context);
            final File cfile = new File(img_path);

            final ExcelImportor importor = new ExcelImportor();
            final ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            final Thread finishThread = new Thread(new Runnable() {
                public void run() {

                    //Toast.makeText(SelectFileActivity.this, "数据量"+mData.size()+"...", Toast.LENGTH_SHORT).show();
                    application.setmData(mData);

                    Intent i = new Intent(SelectFileActivity.this, MainActivity.class );
                    Bundle bundle = new Bundle();
                    bundle.putString("name", cfile.getName());

                    bundle.putString("date", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(rDate));
                    i.putExtras(bundle);
                    setResult(RESULT_OK,i); //  向上一个活动返回数据
                    finish();


                }
            });

            new Thread(new Runnable() {
                public void run() {

                    try {
                        mData = importor.importFromExcel(cfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //progressBar.setVisibility(View.INVISIBLE);
                    finishThread.start();
                }
            }).start();




        }

    }



    private static String getFilePathForN(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }
}