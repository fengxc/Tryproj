package com.example.tryproj;

import java.io.Serializable;

public class PDItem implements Serializable {

    private String sn;
    private String itemName;
    private String locateString;
    private String typeString;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PDItem(String itemName, String locateString) {
        this.itemName = itemName;
        this.locateString = locateString;
        status = false;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLocateString() {
        return locateString;
    }

    public void setLocateString(String locateString) {
        this.locateString = locateString;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }
}
