package com.nfschina.pdScan.dao;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.nfschina.pdScan.PDItem;

@Database(entities = {PDItem.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverters.class})
public abstract class PDItemDataBase extends RoomDatabase {
    public abstract PDDao pdDao();
}
