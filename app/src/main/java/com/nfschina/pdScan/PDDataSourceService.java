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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PDDataSourceService extends Service {

    private PDItemDataBase db;
    private PDDao dao;
    private PDDto dto;


    private int viewStatus;
    private int deptIndex;
    private Map<Integer, String> deptIndexMap;
    private PDItem[] pdData;

    private PDQueryBinder cur = new PDQueryBinder();
    @Override
    public void onCreate() {
        Log.i("service:","onCreate");
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(), PDItemDataBase.class, "pdDatabase").build();
        deptIndexMap = new LinkedHashMap<>();
        dao = db.pdDao();
        dto = new PDDto();
        query();
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
        Intent intent = new Intent("com.nfschina.pdScan.PDDataSourceService_ListUpdate");
        sendBroadcast(intent);
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
        if(viewStatus == 0 ) {
            pdData = dao.loadAllFilteredPDItems(dto.getSn(), dto.getType(), dto.getMark(), dto.getUser(), dto.getLocate(), dto.getPurchaseTimeStart(), dto.getPurchaseTimeEnd(), deptString);
        } else if(viewStatus == 1) {
            pdData = dao.loadDoneFilteredPDItems(dto.getSn(), dto.getType(), dto.getMark(), dto.getUser(), dto.getLocate(), dto.getPurchaseTimeStart(), dto.getPurchaseTimeEnd(), deptString);
        } else {
            pdData = dao.loadUndoneFilteredPDItems(dto.getSn(), dto.getType(), dto.getMark(), dto.getUser(), dto.getLocate(), dto.getPurchaseTimeStart(), dto.getPurchaseTimeEnd(), deptString);
        }

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
        public void importPDList(ArrayList<PDItem> list){
            importNewPD(list);
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


    }

}
