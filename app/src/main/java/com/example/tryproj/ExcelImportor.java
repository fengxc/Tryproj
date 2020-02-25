package com.example.tryproj;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ExcelImportor {
    LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();

    public ExcelImportor() {
        fieldMap .put("序号","sort");
        fieldMap .put("编号","sn");
        fieldMap .put("存放地点","locateString");
        fieldMap .put("设备分类","typeString");
        fieldMap .put("规格型号","markString");
        fieldMap .put("采购方式","purchaseModeString");
        fieldMap .put("单位","unitString");
        fieldMap .put("数量","quantityString");
        fieldMap .put("单价（元）","priceString");
        fieldMap .put("使用人","userString");
        fieldMap .put("责任人","principalString");
        fieldMap .put("购置时间","purchaseTimeString");
        fieldMap .put("涉密情况","securityString");
        fieldMap .put("密级","securityLevelString");
        fieldMap .put("设备状态","eqStateString");
        fieldMap .put("报废日期","scrappedTimeString");
        fieldMap .put("凭证号","docSnString");
        fieldMap .put("备注","memoString");
        fieldMap .put("入库时间","inTimeString");
        fieldMap .put("报废去向","scrappedWayString");
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
                }

            }
            r.add(pd);
        }
        return r;
    }
}
