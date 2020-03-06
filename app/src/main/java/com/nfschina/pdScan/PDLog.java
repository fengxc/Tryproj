package com.nfschina.pdScan;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "PDLog")
public class PDLog {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date scanDate;

    private String sn;

    private String conflictLog;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getScanDate() {
        return scanDate;
    }

    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getConflictLog() {
        return conflictLog;
    }

    public void setConflictLog(String conflictLog) {
        this.conflictLog = conflictLog;
    }
}
