package com.xuanxinszp.ssjdemo.common.util;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 读取工具类
 *
 * @author: Benson Xu
 * @date: 2018年05月01日 17:52
 */
public class ExcelReaderUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelReaderUtil.class);

    /**
     * xlsx 表格读取
     * @param excelFile Excel文件
     * @return
     * @throws Exception
     */
    public static List<List<String>> readerXlsx(File excelFile) throws Exception {
        if (null == excelFile) {
            throw new Exception("Excel文件不能为空");
        }
        // 读取文件
        InputStream is = new FileInputStream(excelFile);

        return readerXlsx(is);
    }

    /**
     * xlsx 表格读取
     * @param excelUrl Excel文件路径
     * @return
     * @throws Exception
     */
    public static List<List<String>> readerXlsx(String excelUrl) throws Exception {
        if (!excelUrl.endsWith(".xlsx")) {
            throw new Exception("目前仅支持Excel 2007版本或者以上的版本");
        }
        // 读取文件
        File excelFile = new File(excelUrl);
        InputStream is = new FileInputStream(excelFile);

        return readerXlsx(is);
    }


    /**
     * xlsx 表格读取
     * @param is Excel 文件流
     * @return
     * @throws Exception
     */
    public static List<List<String>> readerXlsx(InputStream is) throws Exception {
        // 读取xlsx文件
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

        if (xssfWorkbook == null) {
            System.out.println("未读取到内容,请检查路径！");
            return null;
        }

        List<List<String>> ans = new ArrayList<List<String>>();
        // 遍历xlsx中的sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // 对于每个sheet，读取其中的每一行
            for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow == null) continue;
                ArrayList<String> curarr = new ArrayList<String>();
                for (int columnNum = 0; columnNum < xssfRow.getLastCellNum(); columnNum++) {
                    XSSFCell cell = xssfRow.getCell(columnNum);
                    curarr.add(trimStr(getValue(cell)));
                }
                ans.add(curarr);
            }
        }
        return ans;
    }


    /**
     * 判断后缀为xlsx的excel文件的数据类
     * @param xssfCell
     * @return
     */
    @SuppressWarnings("deprecation")
    private static String getValue(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "---";
        }
        logger.info("====================>>>>sxxfcell={}",xssfCell.toString());
        if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
            return numericFormat(xssfCell);
        } else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BLANK || xssfCell.getCellType() == XSSFCell.CELL_TYPE_ERROR) {
            return "---";
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }


    /**
     * 数值格式化
     * @param xssfCell
     * @return
     */
    private static String numericFormat(XSSFCell xssfCell) {
        double cur = xssfCell.getNumericCellValue();
        long longVal = Math.round(cur);
        Object inputValue = null;
        if (Double.parseDouble(longVal + ".0") == cur)
            inputValue = longVal;
        else
            inputValue = cur;
        return String.valueOf(inputValue);
    }


    /**
     * 字符串修剪  去除所有空白符号 ， 问号 ， 中文空格
     * @param str
     * @return
     */
    public static String trimStr(String str){
        if(str==null)
            return null;
        return str.replaceAll("[\\s\\?]", "").replace("　", "");
    }
}
