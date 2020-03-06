package com.nfschina.pdScan.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nfschina.pdScan.PDItem;
import com.nfschina.pdScan.PDLog;

import java.sql.Date;

@Dao
public interface PDDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPDItem(PDItem pdItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPDItems(PDItem[] list);

    @Update
    public void updatePDItem(PDItem pdItem);

    @Update
    public void updatePDItems(PDItem[] list);

    @Query("SELECT * FROM PDItem")
    public PDItem[] loadPDItems();

    @Query("SELECT * FROM PDItem WHERE sn = :sn")
    public PDItem[] findPDItem(String sn);

    @Query("SELECT * FROM PDItem WHERE status = 1")
    public PDItem[] loadDonePDItems();

    @Query("SELECT * FROM PDItem WHERE status = 0")
    public PDItem[] loadUndonePDItems();




    @Delete
    public void deletePDItem(PDItem pdItem);


    @Delete
    public void deletePDItems(PDItem[] pdItem);

    @Query("SELECT * FROM PDItem WHERE (sn like :sn or (:sn is null)) and (typeString like :type or (:type is null)) and (markString like :mark or (:mark is null)) and (userString like :user or (:user is null)) and (locateString like :locate or (:locate is null)) and (deptString like :dept or (:dept is null)) and (purchaseTime >= :start or (:start is null)) and (purchaseTime < :end or (:end is null))  ")
    public PDItem[] loadAllFilteredPDItems(String  sn, String type, String mark, String user, String locate, Date start, Date end, String dept);

    @Query("SELECT * FROM PDItem WHERE (sn like :sn or (:sn is null)) and (typeString like :type or (:type is null)) and (markString like :mark or (:mark is null)) and (userString like :user or (:user is null)) and (locateString like :locate or (:locate is null)) and (deptString like :dept or (:dept is null)) and (purchaseTime >= :start or (:start is null)) and (purchaseTime < :end or (:end is null)) and status = 1")
    public PDItem[] loadDoneFilteredPDItems(String  sn, String type, String mark, String user, String locate, Date start, Date end, String dept);

    @Query("SELECT * FROM PDItem WHERE (sn like :sn or (:sn is null)) and (typeString like :type or (:type is null)) and (markString like :mark or (:mark is null)) and (userString like :user or (:user is null)) and (locateString like :locate or (:locate is null)) and (deptString like :dept or (:dept is null)) and (purchaseTime >= :start or (:start is null)) and (purchaseTime < :end or (:end is null)) and status = 0")
    public PDItem[] loadUndoneFilteredPDItems(String  sn, String type, String mark, String user, String locate, Date start, Date end, String dept);

    @Query("SELECT count(*) FROM PDItem WHERE (sn like :sn or (:sn is null)) and (typeString like :type or (:type is null)) and (markString like :mark or (:mark is null)) and (userString like :user or (:user is null)) and (locateString like :locate or (:locate is null)) and (deptString like :dept or (:dept is null)) and (purchaseTime >= :start or (:start is null)) and (purchaseTime < :end or (:end is null))  ")
    public int countAllFilteredPDItems(String  sn, String type, String mark, String user, String locate, Date start, Date end, String dept);

    @Query("SELECT count(*) FROM PDItem WHERE (sn like :sn or (:sn is null)) and (typeString like :type or (:type is null)) and (markString like :mark or (:mark is null)) and (userString like :user or (:user is null)) and (locateString like :locate or (:locate is null)) and (deptString like :dept or (:dept is null)) and (purchaseTime >= :start or (:start is null)) and (purchaseTime < :end or (:end is null)) and status = 1")
    public int countDoneFilteredPDItems(String  sn, String type, String mark, String user, String locate, Date start, Date end, String dept);

    @Query("SELECT count(*) FROM PDItem WHERE (sn like :sn or (:sn is null)) and (typeString like :type or (:type is null)) and (markString like :mark or (:mark is null)) and (userString like :user or (:user is null)) and (locateString like :locate or (:locate is null)) and (deptString like :dept or (:dept is null)) and (purchaseTime >= :start or (:start is null)) and (purchaseTime < :end or (:end is null)) and status = 0")
    public int countUndoneFilteredPDItems(String  sn, String type, String mark, String user, String locate, Date start, Date end, String dept);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertPDLog(PDLog pdLog);

    @Query("SELECT * FROM PDLog")
    public PDLog[] getAllPDLog();



    @Delete
    public void deletePDLog(PDLog[] pdLogs);

}
