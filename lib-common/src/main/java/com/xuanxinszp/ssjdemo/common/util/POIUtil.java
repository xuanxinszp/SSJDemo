package com.xuanxinszp.ssjdemo.common.util;


import com.xuanxinszp.ssjdemo.common.annotation.ExportExcel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by 6212
 */
public class POIUtil<T> {

    private static final Logger logger = LoggerFactory.getLogger(POIUtil.class);

    /**
     * 导出2007excel数据
     * <br/>
     * @param outputStream 输出文件流
     * @throws IOException
     */
    public static<T> void Excel2007AboveOperate(OutputStream outputStream, List<T> list) throws Exception {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(workbook, 100);
            //第一个表格
            Sheet firstSheet = sxssfWorkbook.createSheet();
            XSSFCellStyle  cellStyle= setCellStyle(workbook);
            buildCellData(firstSheet,cellStyle, list);
            sxssfWorkbook.write(outputStream);
        } finally {
            if (Objects.nonNull(outputStream)) {
                outputStream.close();
            }
        }
    }

    /**
     * 导出2007excel数据
     * <br/>
     * @param outputStream 输出文件流
     * @throws IOException
     */
    public static<T> ByteArrayOutputStream Excel2007(ByteArrayOutputStream outputStream, List<T> list) throws Exception {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(workbook, 100);
            //第一个表格
            Sheet firstSheet = sxssfWorkbook.createSheet();
            XSSFCellStyle cellStyle = setCellStyle(workbook);
            buildCellData(firstSheet,cellStyle, list);
            sxssfWorkbook.write(outputStream);
            return outputStream;
        } finally {
            if (Objects.nonNull(outputStream)) {
                outputStream.close();
            }
        }
    }

    /**
     * 构建excel数据
     * @param sheet 表格
     * @param cellStyle 样式
     * @param list 数据集合
     * @param <T> 对象实体
     * @throws Exception
     */
    private static<T> void buildCellData(Sheet sheet, XSSFCellStyle cellStyle, List<T> list) throws Exception{
        if(CollectionUtil.isNotNil(list)){
            Field[] fields = list.get(0).getClass().getDeclaredFields();
            int lenght = fields.length;
            if(lenght == 0) {
                return;
            }
            List<Field> outFields = new ArrayList<>();//需要导出的属性列集合
            //第一行创建表格每列的头部，通过注解ExportExcel获取表头名称, 同时将带有注解的列放到新的Field集合中
            Row row = sheet.createRow(0);

            int x = 0;
            for (int j = 0; j < lenght; j++) {
                ExportExcel excels = fields[j].getDeclaredAnnotation(ExportExcel.class);
                if(Objects.nonNull(excels)) {
                    fields[j].setAccessible(true);
                    CellUtil.createCell(row, x, excels.headName(),cellStyle);
                    sheet.setColumnWidth(j, 10 * 512); //设置列宽
                    outFields.add(fields[j]);
                    x++;
                }
            }
            if(outFields.size() > 0) {
                //创建数据行，需要导出的列数通过outFields集合的长度来定
                for (int i = 0; i < list.size(); i++) {
                    //从第二行开始，因为第一行是表头
                    row = sheet.createRow(i+1);
                    for (int k = 0; k < outFields.size(); k++) {
                        CellUtil.createCell(row, k, getStrFromField(outFields.get(k), list.get(i)),cellStyle);
                    }
                }
            }
        }
    }

    private static XSSFCellStyle setCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        // 设置单元格边框样式
        // BorderStyle.BORDER_DOUBLE      双边线
        // BorderStyle.BORDER_THIN        细边线
        // BorderStyle.BORDER_MEDIUM      中等边线
        // BorderStyle.BORDER_DASHED      虚线边线
        // BorderStyle.BORDER_HAIR        小圆点虚线边线
        // BorderStyle.BORDER_THICK       粗边线
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        // 设置单元格边框颜色
        cellStyle.setBottomBorderColor(new XSSFColor(java.awt.Color.BLACK));
        cellStyle.setTopBorderColor(new XSSFColor(java.awt.Color.BLACK));
        cellStyle.setLeftBorderColor(new XSSFColor(java.awt.Color.BLACK));
        cellStyle.setRightBorderColor(new XSSFColor(java.awt.Color.BLACK));
        //设置对齐格式 -- 靠左
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);

        cellStyle.setWrapText(true); // 设置单元格内容是否自动换行

        return cellStyle;
    }

    private static<T> String getStrFromField(Field field, T obj) throws Exception{
        String str="";
        if(Objects.isNull(field)) {
            return str;
        }
        Object object = field.get(obj);
        if(Objects.isNull(object)) {
            return str;
        }
        if (field.getType().equals(Date.class)) {
            str = TimeUtil.toString((Date) field.get(obj), TimeUtil.dateTimePattern);
        } else {
            str = field.get(obj) +"";
        }
        return str;
    }

}
