package com.example.tryproj;

import java.io.Serializable;

public class PDItem implements Serializable {

    private String sn;
    private String sort;
    private String deptString;
    private String locateString;//存放地点
    private String typeString; //设备分类
    private String markString; //规格型号
    private String purchaseModeString;//采购方式
    private String unitString;//单位
    private String quantityString;//数量
    private String priceString;//单价
    private String userString;//使用人
    private String principalString;//负责人
    private String purchaseTimeString;//购置时间
    private String securityString;//
    private String securityLevelString;//
    private String eqStateString;//设备状态
    private String scrappedTimeString;//报废日期
    private String docSnString;//凭证号
    private String memoString;//备注
    private String inTimeString;//入库时间
    private String scrappedWayString;//报废去向

    private boolean status;

    public PDItem() {
        this.sn = "";
        this.locateString = "";
        this.status = false;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PDItem(String sn, String locateString) {
        this.sn = sn;
        this.locateString = locateString;
        this.status = false;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

//    public String getItemName() {
//        return itemName;
//    }
//
//    public void setItemName(String itemName) {
//        this.itemName = itemName;
//    }

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

    public String getMarkString() {
        return markString;
    }

    public void setMarkString(String markString) {
        this.markString = markString;
    }

    public String getPurchaseModeString() {
        return purchaseModeString;
    }

    public void setPurchaseModeString(String purchaseModeString) {
        this.purchaseModeString = purchaseModeString;
    }

    public String getUnitString() {
        return unitString;
    }

    public void setUnitString(String unitString) {
        this.unitString = unitString;
    }

    public String getQuantityString() {
        return quantityString;
    }

    public void setQuantityString(String quantityString) {
        this.quantityString = quantityString;
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public String getUserString() {
        return userString;
    }

    public void setUserString(String userString) {
        this.userString = userString;
    }

    public String getPrincipalString() {
        return principalString;
    }

    public void setPrincipalString(String principalString) {
        this.principalString = principalString;
    }

    public String getPurchaseTimeString() {
        return purchaseTimeString;
    }

    public void setPurchaseTimeString(String purchaseTimeString) {
        this.purchaseTimeString = purchaseTimeString;
    }

    public String getSecurityString() {
        return securityString;
    }

    public void setSecurityString(String securityString) {
        this.securityString = securityString;
    }

    public String getSecurityLevelString() {
        return securityLevelString;
    }

    public void setSecurityLevelString(String securityLevelString) {
        this.securityLevelString = securityLevelString;
    }

    public String getEqStateString() {
        return eqStateString;
    }

    public void setEqStateString(String eqStateString) {
        this.eqStateString = eqStateString;
    }

    public String getScrappedTimeString() {
        return scrappedTimeString;
    }

    public void setScrappedTimeString(String scrappedTimeString) {
        this.scrappedTimeString = scrappedTimeString;
    }

    public String getDocSnString() {
        return docSnString;
    }

    public void setDocSnString(String docSnString) {
        this.docSnString = docSnString;
    }

    public String getMemoString() {
        return memoString;
    }

    public void setMemoString(String memoString) {
        this.memoString = memoString;
    }


    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getInTimeString() {
        return inTimeString;
    }

    public void setInTimeString(String inTimeString) {
        this.inTimeString = inTimeString;
    }

    public String getScrappedWayString() {
        return scrappedWayString;
    }

    public void setScrappedWayString(String scrappedWayString) {
        this.scrappedWayString = scrappedWayString;
    }

    public String getDeptString() {
        return deptString;
    }

    public void setDeptString(String deptString) {
        this.deptString = deptString;
    }
}
