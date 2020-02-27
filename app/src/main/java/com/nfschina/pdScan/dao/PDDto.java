package com.nfschina.pdScan.dao;

import java.io.Serializable;
import java.sql.Date;

public class PDDto  implements Serializable {
    private String sn;
    private String type;
    private String mark;
    private String user;
    private String locate;
    private Date purchaseTimeStart;
    private Date purchaseTimeEnd;


    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }

    public Date getPurchaseTimeStart() {
        return purchaseTimeStart;
    }

    public void setPurchaseTimeStart(Date purchaseTimeStart) {
        this.purchaseTimeStart = purchaseTimeStart;
    }

    public Date getPurchaseTimeEnd() {
        return purchaseTimeEnd;
    }

    public void setPurchaseTimeEnd(Date purchaseTimeEnd) {
        this.purchaseTimeEnd = purchaseTimeEnd;
    }
}
