package com.nfschina.pdScan;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class ExcelImportor {
    LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
    LinkedHashMap<String, String> dateFieldMap = new LinkedHashMap<>();

    public ExcelImportor() {
        fieldMap .put("序号","sort");
        fieldMap .put("编号","sn");
        fieldMap .put("所属部门","deptString");
        fieldMap .put("存放地点","locateString");
        fieldMap .put("设备分类","typeString");
        fieldMap .put("规格型号","markString");
        fieldMap .put("采购方式","purchaseModeString");
        fieldMap .put("单位","unitString");
        fieldMap .put("数量","quantityString");
        fieldMap .put("单价（元）","priceString");
        fieldMap .put("使用人","userString");
        fieldMap .put("责任人","principalString");
        fieldMap .put("涉密情况","securityString");
        fieldMap .put("密级","securityLevelString");
        fieldMap .put("设备状态","eqStateString");
        fieldMap .put("凭证号","docSnString");
        fieldMap .put("备注","memoString");
        fieldMap .put("报废去向","scrappedWayString");
        fieldMap .put("保密编号","securitySNString");
        fieldMap .put("硬盘序列号","HDSNString");

        dateFieldMap .put("购置时间","purchaseTime");
        dateFieldMap .put("报废日期","scrappedTime");
        dateFieldMap .put("入库时间","inTime");

    }

    public ArrayList<PDItem> inportFromExcel(File cfile) throws IOException {
        FileInputStream file = null;
        file = new FileInputStream(cfile);
        Workbook wb = new HSSFWorkbook(file);
        Sheet sheet = wb.getSheetAt(0);
        int num = sheet.getLastRowNum();
        Row rowName = sheet.getRow(0);

        ArrayList<PDItem> r = new ArrayList<PDItem>();
        for (int index=1;index<=num;index++){
            PDItem pd = new PDItem();
            Row row = sheet.getRow(index);
            for(int cindex =0;cindex<row.getLastCellNum();cindex++){
                String columnName = rowName.getCell(cindex).getStringCellValue();
                String fieldName = fieldMap.get(columnName);
                String dateFieldName = dateFieldMap.get(columnName);
                if(fieldName!=null){
                    String methodStr = "set"+fieldName.toUpperCase().substring(0, 1)+fieldName.substring(1);

                    if (row.getCell(cindex)!=null){
                        if(row.getCell(cindex).toString().length()>0){
                            String arg = row.getCell(cindex).toString();
                            try {
                                Method method1 = pd.getClass().getMethod(methodStr, new Class[] { String.class });
                                method1.invoke(pd,arg);
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }else{

                        }
                    }
                }else if(dateFieldName!=null){
                    String methodStr = "set"+dateFieldName.toUpperCase().substring(0, 1)+dateFieldName.substring(1);

                    if (row.getCell(cindex)!=null){
                        if(row.getCell(cindex).toString()!=null&&row.getCell(cindex).toString().length()>0){
                            try {
                                Date arg = row.getCell(cindex).getDateCellValue();

                                try {
                                    Method method1 = pd.getClass().getMethod(methodStr, new Class[]{java.sql.Date.class});
                                    method1.invoke(pd, new java.sql.Date(arg.getTime()));
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                String argStr = row.getCell(cindex).getStringCellValue();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                try{
                                    Date s = sdf.parse(argStr);
                                    Method method1 = pd.getClass().getMethod(methodStr, new Class[]{java.sql.Date.class});
                                    method1.invoke(pd, new java.sql.Date(s.getTime()));
                                } catch (ParseException | NoSuchMethodException ex) {
                                    ex.printStackTrace();
                                } catch (IllegalAccessException ex) {

                                } catch (InvocationTargetException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }else{

                        }
                    }
                }

            }
            pd.setSn(pd.getSn().toUpperCase());
            if(!"报废".equals(pd.getEqStateString()))
                r.add(pd);
        }
        return r;
    }
}
