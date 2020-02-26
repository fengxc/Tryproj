package com.nfschina.pdScan.dao;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.nfschina.pdScan.PDItem;

@Database(entities = {PDItem.class}, version = 1, exportSchema = false)
public abstract class PDItemDataBase extends RoomDatabase {
    public abstract PDDao pdDao();
}
