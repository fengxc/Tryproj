package com.nfschina.pdScan;


import android.app.Application;

import java.util.ArrayList;

public class PDApplication extends Application {
    private ArrayList<PDItem> mData;

    public ArrayList<PDItem> getmData() {
        return mData;
    }

    public void setmData(ArrayList<PDItem> mData) {
        this.mData = mData;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mData=new ArrayList<>();
    }
}
