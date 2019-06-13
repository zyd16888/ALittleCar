package com.clover.common.util.excel;



import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

/**
 * @author : 丁鹏飞
 * Date : 2018/3/9 11:40
 **/
public class WriteExcel {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";



    public static void writeExcel(List<String> dataList, String separator,int startRow, String path) {
        OutputStream out = null;
        try {

            // 读取Excel文档
            File file = new File(path);
            Workbook workBook = getWorkbok(file);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);
            /*
             * 删除原有数据，除了属性列
             */
            int rowNumber = sheet.getLastRowNum();    // 第一行从0开始算
            System.out.println("原始数据总行数，除属性列：" + rowNumber);
            for (int i = startRow; i <= rowNumber; i++) {
                Row row = sheet.getRow(i);
                sheet.removeRow(row);
            }
            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(path);
            workBook.write(out);

            workBook = getWorkbok(file);
            sheet = workBook.getSheetAt(0);
            /*
             * 往Excel中写新数据
             */
            for (int j = 0; j < dataList.size(); j++) {
                // 创建一行：从第二行开始，跳过属性列
                Row row = sheet.createRow(j + startRow);
                // 得到要插入的每一条记录
                if (dataList.get(j).contains(separator)) {
                    String[] arr = dataList.get(j).split(separator);
                    for (int i = 0; i < arr.length; i++) {
                        Cell cell = row.createCell(i);
                        cell.setCellValue(arr[i]);
                    }
                } else {
                    Cell cell = row.createCell(0);
                    cell.setCellValue(dataList.get(j));
                }
            }
            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out = new FileOutputStream(path);
            workBook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功");
    }


    public static Workbook getWorkbok(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if (file.getName().endsWith(EXCEL_XLS)) {     //Excel&nbsp;2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {    // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }
}
