package com.example.tryproj.dao;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tryproj.PDItem;

@Database(entities = {PDItem.class}, version = 1)
public abstract class PDItemDataBase extends RoomDatabase {
    public abstract PDDao pdDao();
}
