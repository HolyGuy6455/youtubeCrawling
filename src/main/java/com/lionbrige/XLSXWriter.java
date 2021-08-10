package com.lionbrige;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * XLSXWriter
 */
public class XLSXWriter {

    private Workbook xlsxWb;
    private Sheet sheet;
    private int rowIndex;
    private CellStyle cellStyle;

    private final static String HEADER_TEXT[] = new String[]{
        "",
        "제품",
        "주요분류",
        "세부분류",
        "이슈",
        "채널",
        "종류",
        "조회수",
        "공감수",
        "댓글수",
        "제목/내용",
        "관련발화",
        "URL",
        "작성일",
        "평가",
        "당사지원",
        "비고",
        "작업일"
    };

    private final static XLSXWriter singleton;
    static{
        singleton = new XLSXWriter();
    }

    private XLSXWriter(){
        xlsxWb = new XSSFWorkbook();
        cellStyle = xlsxWb.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
    }

    public void newSheet(String sheetName) {
        sheet = xlsxWb.createSheet(sheetName);
        sheet.setColumnWidth(10, 20000);
        rowIndex = 2;
 
        Row row = sheet.createRow(1);
        
        for (int i = 0; i < HEADER_TEXT.length; i++) {
            row.createCell(i).setCellValue(HEADER_TEXT[i]);
        }
    }

    /**
     * @return the singleton
     */
    public static XLSXWriter getInstance() {
        return singleton;
    }

    public void add(List<YTDComments> commentsList) {

        for (YTDComments comment : commentsList) {
            Row row = sheet.createRow(rowIndex++);
            row.setHeight((short) 1000);
            row.createCell(5).setCellValue("Youtube");;
            row.createCell(6).setCellValue("댓글");;
            Cell contentCell = row.createCell(10);
            Cell urlCell = row.createCell(12);

            contentCell.setCellValue(comment.getUserName() + "\r\n" + comment.getContent());
            contentCell.setCellStyle(cellStyle);
            urlCell.setCellValue(comment.getStrURL());
        }
    }

    public void save() {
        File file = new File("./result.xlsx");
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            xlsxWb.write(outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            xlsxWb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}