package com.nfschina.pdScan;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.room.Room;

import com.nfschina.pdScan.dao.PDDao;
import com.nfschina.pdScan.dao.PDDto;
import com.nfschina.pdScan.dao.PDItemDataBase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PDDataSourceService extends Service {

    private PDItemDataBase db;
    private PDDao dao;
    private PDDto dto;
    private int numofAll = 0 ;
    private int numofDone = 0;
    private int numofUnDone = 0;

    private int viewStatus;
    private int deptIndex;
    private Map<Integer, String> deptIndexMap;
    private PDItem[] pdData;
    private String excelName;
    private String excelDate;
    private PDItem[] pdItemSNQueryResult;

    private PDQueryBinder cur = new PDQueryBinder();


    private SharedHelper helper;
    @Override
    public void onCreate() {
        Log.i("service:","onCreate");
        super.onCreate();
        helper = new SharedHelper(getApplicationContext());
        db = Room.databaseBuilder(getApplicationContext(), PDItemDataBase.class, "pdDatabase.db").build();
        deptIndexMap = new LinkedHashMap<>();
        dao = db.pdDao();
        dto = new PDDto();
        if(helper.contains("deptIndexMap")){
            String tString = (String) helper.get("deptIndexMap","");
            deptIndexMap = getMapFromString(tString);
        }
        if(helper.contains("excelName")){
            excelName = (String) helper.get("excelName","");
        }
        if(helper.contains("excelDate")){
            excelDate = (String) helper.get("excelDate","");
        }
        if(helper.contains("viewStatus")){
            viewStatus = (Integer) helper.get("viewStatus",new Integer(0));
        }
        if(helper.contains("deptIndex")){
            deptIndex = (Integer) helper.get("deptIndex",new Integer(0));
        }
        if(helper.contains("dto."+"sn")){
            String string = (String) helper.get("dto."+"sn","");
            if(string!=null&&string.length()>0)
                dto.setSn(string);
        }
        if(helper.contains("dto."+"type")){
            String string = (String) helper.get("dto."+"type","");
            if(string!=null&&string.length()>0)
                dto.setType(string);
        }
        if(helper.contains("dto."+"mark")){
            String string = (String) helper.get("dto."+"mark","");
            if(string!=null&&string.length()>0)
                dto.setMark(string);
        }
        if(helper.contains("dto."+"user")){
            String string = (String) helper.get("dto."+"user","");
            if(string!=null&&string.length()>0)
                dto.setUser(string);
        }
        if(helper.contains("dto."+"locate")){
            String string = (String) helper.get("dto."+"locate","");
            if(string!=null&&string.length()>0)
                dto.setLocate(string);
        }
        if(helper.contains("dto."+"purchaseTimeStart")){
            Long longValue = (Long) helper.get("dto."+"purchaseTimeStart",new Long(0L));
            if(longValue!=null&&longValue>0)
                dto.setPurchaseTimeStart(new Date(longValue));
        }
        if(helper.contains("dto."+"purchaseTimeEnd")){
            Long longValue = (Long) helper.get("dto."+"purchaseTimeEnd",new Long(0L));
            if(longValue!=null&&longValue>0)
                dto.setPurchaseTimeEnd(new Date(longValue));
        }
        query();
    }

    public int getNumofAll() {
        return numofAll;
    }

    public int getNumofDone() {
        return numofDone;
    }

    public int getNumofUnDone() {
        return numofUnDone;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public String getExcelDate() {
        return excelDate;
    }

    public void setExcelDate(String excelDate) {
        this.excelDate = excelDate;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("service:","onStart");

        return super.onStartCommand(intent, flags, startId);
    }

    public void query() {
        new Thread(new Runnable() {
            public void run() {
                PDItem[] newList = dao.loadPDItems();
                deptIndexMap.clear();
                deptIndexMap.put(0,"全部");
                int i=1;
                List<String> alreadyDep = new ArrayList<>();
                for(PDItem p:newList){
                    if(!alreadyDep.contains(p.getDeptString())){
                        alreadyDep.add(p.getDeptString());
                        deptIndexMap.put(i,p.getDeptString());
                        i++;
                    }
                }
                execQuery();
                refreshData();
            }
        }).start();
    }

    public void refreshData() {
        helper.put("excelName",excelName);
        helper.put("excelDate",excelDate);
        helper.put("viewStatus",viewStatus);
        helper.put("deptIndex",deptIndex);
        StringBuffer sString = getValueStringFromMap(deptIndexMap);
        helper.put("deptIndexMap", sString.toString());
        helper.put("dto."+"sn",dto.getSn());
        helper.put("dto."+"type",dto.getType());
        helper.put("dto."+"mark",dto.getMark());
        helper.put("dto."+"user",dto.getUser());
        helper.put("dto."+"locate",dto.getLocate());
        if(dto.getPurchaseTimeStart()!=null)
            helper.put("dto."+"purchaseTimeStart",dto.getPurchaseTimeStart().getTime());
        else
            helper.put("dto."+"purchaseTimeStart",null);
        if(dto.getPurchaseTimeEnd()!=null)
            helper.put("dto."+"purchaseTimeEnd",dto.getPurchaseTimeEnd().getTime());
        else
            helper.put("dto."+"purchaseTimeEnd",null);

        Intent intent = new Intent("com.nfschina.pdScan.PDDataSourceService_ListUpdate");
        sendBroadcast(intent);
    }

    public static Map<Integer, String> getMapFromString(String st) {
        Map<Integer, String> rMap = new LinkedHashMap<>();
        String[] splited = st.split(",");
        int i = 0;
        for(String s:splited){
            rMap.put(i, s);
            i++;
        }
        return rMap;
    }


    public static StringBuffer getValueStringFromMap(Map<Integer, String> deptIndexMap) {
        Iterator entries = deptIndexMap.entrySet().iterator();
        StringBuffer sString =new StringBuffer("");
        while (entries.hasNext()) {

            Map.Entry entry = (Map.Entry) entries.next();

            String value = (String) entry.getValue();

            sString.append(value).append(",");

        }
        if(sString.length()>=1)
            sString.deleteCharAt(sString.length()-1);
        return sString;
    }


    public PDDto getDto() {
        return dto;
    }

    public void setDto(PDDto dto) {
        this.dto = dto;
    }

    private void execQuery() {
        String deptString = null;
        if(deptIndex>0) {
            deptString = deptIndexMap.get(deptIndex);
        }
        numofAll = dao.countAllFilteredPDItems(dto.getSn(), dto.getType(), dto.getMark(), dto.getUser(), dto.getLocate(), dto.getPurchaseTimeStart(), dto.getPurchaseTimeEnd(), deptString);

        numofDone = dao.countDoneFilteredPDItems(dto.getSn(), dto.getType(), dto.getMark(), dto.getUser(), dto.getLocate(), dto.getPurchaseTimeStart(), dto.getPurchaseTimeEnd(), deptString);

        numofUnDone = dao.countUndoneFilteredPDItems(dto.getSn(), dto.getType(), dto.getMark(), dto.getUser(), dto.getLocate(), dto.getPurchaseTimeStart(), dto.getPurchaseTimeEnd(), deptString);

        if(viewStatus == 0 ) {
            pdData = dao.loadAllFilteredPDItems(dto.getSn(), dto.getType(), dto.getMark(), dto.getUser(), dto.getLocate(), dto.getPurchaseTimeStart(), dto.getPurchaseTimeEnd(), deptString);
        } else if(viewStatus == 1) {
            pdData = dao.loadDoneFilteredPDItems(dto.getSn(), dto.getType(), dto.getMark(), dto.getUser(), dto.getLocate(), dto.getPurchaseTimeStart(), dto.getPurchaseTimeEnd(), deptString);
        } else {
            pdData = dao.loadUndoneFilteredPDItems(dto.getSn(), dto.getType(), dto.getMark(), dto.getUser(), dto.getLocate(), dto.getPurchaseTimeStart(), dto.getPurchaseTimeEnd(), deptString);
        }

    }

    private void updatePDItemConflictLog(final PDItem pdItem) {
        new Thread(new Runnable() {
            public void run() {
                dao.updatePDItem(pdItem);
                if (pdItem.getConflictLog()!=null&&pdItem.getConflictLog().length()>0){
                    String str = pdItem.getSn();
                    PDLog pdLog = new PDLog();
                    pdLog.setScanDate(new Date(System.currentTimeMillis()));
                    pdLog.setSn(str);
                    pdLog.setConflictLog(pdItem.getConflictLog());
                    dao.insertPDLog(pdLog);
                }
                execQuery();
                refreshData();

            }
        }).start();
    }


        private void checkPDItemBySn(final String sn) {
        new Thread(new Runnable() {
            public void run() {
                PDItem[] rl = dao.findPDItem(sn);
                for(PDItem r:rl){
                    r.setStatus(true);
                }
                dao.updatePDItems(rl);
                execQuery();
                refreshData();
            }
        }).start();
    }

    public void importNewPD(final ArrayList<PDItem> list){
        new Thread(new Runnable() {
            public void run() {
                PDItem[] oldData = dao.loadPDItems();
                dao.deletePDItems(oldData);
                for(PDItem i:list) {
                    dao.insertPDItem(i);
                }
                PDLog[] oldData2 = dao.getAllPDLog();
                dao.deletePDLog(oldData2);
                PDItem[] newList = dao.loadPDItems();
                deptIndexMap.clear();
                deptIndexMap.put(0,"全部");
                int i=1;
                List<String> alreadyDep = new ArrayList<>();
                for(PDItem p:newList){
                    if(!alreadyDep.contains(p.getDeptString())){
                        alreadyDep.add(p.getDeptString());
                        deptIndexMap.put(i,p.getDeptString());
                        i++;
                    }
                }
                execQuery();
                refreshData();
            }
        }).start();
    }

    private void insertPDLog(final PDLog pdLog) {
        new Thread(new Runnable() {
            public void run() {
                dao.insertPDLog(pdLog);

            }
        }).start();
    }


    public void queryBySN(final String sn) {
        new Thread(new Runnable() {
            public void run() {
                pdItemSNQueryResult = dao.findPDItem(sn);
                if (pdItemSNQueryResult == null||pdItemSNQueryResult.length==0){
                    String str=sn;
                    PDLog pdLog = new PDLog();
                    pdLog.setScanDate(new Date(System.currentTimeMillis()));
                    pdLog.setSn(str);
                    pdLog.setConflictLog(getString(R.string.pdlog_notfound));
                    dao.insertPDLog(pdLog);

                }
                Intent intent = new Intent("com.nfschina.pdScan.PDDataSourceService_PDItemUpdate");
                sendBroadcast(intent);
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service:","onBind");

        return cur;
    }

    class PDQueryBinder extends Binder {

        public List<PDItem> getCurrentResult() {
            List<PDItem> list = new ArrayList<>();
            if(pdData!=null) {
                for (PDItem p : pdData)
                    list.add(p);
            }
            return list;
        }

        public void checkPDItem(String sn){
            checkPDItemBySn(sn);
        }

        public void changeStatus(int i){
            viewStatus = i;
            query();

        }

        public void changeDept(int i){
            deptIndex = i;
            query();
        }

        public void changeSearchConditions(String  sn, String type, String mark, String user, String locate, Date start, Date end){
            dto.setSn(sn);
            dto.setType(type);
            dto.setMark(mark);
            dto.setUser(user);
            dto.setLocate(locate);
            dto.setPurchaseTimeStart(start);
            dto.setPurchaseTimeEnd(end);
            query();
        }
        public void importPDList(ArrayList<PDItem> list, String name, String dateString){
            importNewPD(list);
            excelName = name;
            excelDate = dateString;
        }


        public PDDto pullDto() {
            return getDto();
        }

        public void pushDto(PDDto dto) {
            setDto(dto);
        }

        public void refresh(){
            query();
            refreshData();
        }
        public Map getMap(){
            return deptIndexMap;
        }

        public String getExcelName() {
            return excelName;
        }

        public String getExcelDate() {
            return excelDate;
        }

        public int getViewStatus(){
            return viewStatus;
        }

        public int getDeptIndex() {
            return deptIndex;
        }

        public void queryPdItemBySN(String sn){
            queryBySN(sn);
        }

        public PDItem[] getPdItemSNQueryResult() {
            return pdItemSNQueryResult;
        }

        public void updateConflictLog(PDItem pdItem){updatePDItemConflictLog(pdItem);}

        public void insertPDLog(PDLog pdLog){PDDataSourceService.this.insertPDLog(pdLog);}

        public PDLog[] getPdLogsForExport(){
            return dao.getAllPDLog();
        }
        public PDItem[] getPdItemsForExport(){
            return dao.loadDonePDItems();
        }

        public int getNumofAll() {
            return numofAll;
        }

        public int getNumofDone() {
            return numofDone;
        }

        public int getNumofUnDone() {
            return numofUnDone;
        }
    }


}
