package com.lionbrige;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * XLSXWriter
 */
public class XLSXReader {

    public static List<URLBundle> read(File file) {
        
        try {
            Workbook xlsxWb = new XSSFWorkbook(file);
            List<URLBundle> result = new ArrayList<URLBundle>();

            for (Sheet sheet : xlsxWb) {
                URLBundle bundle = new URLBundle(sheet.getSheetName());
                int urlColumn = -1;
                Row firstRow = sheet.getRow(0);

                if(firstRow == null){
                    continue;
                }

                for (Cell cell : firstRow) {
                    if (cell.getStringCellValue().compareTo("URL") == 0) {
                        urlColumn = cell.getColumnIndex();
                        System.out.println(urlColumn);
                        break;
                    }
                }
                if(urlColumn == -1){
                    continue;
                }
                List<String> urlList = bundle.getUrlList();
                Iterator<Row> rowIterator = sheet.rowIterator();
                rowIterator.next(); // 맨 첫번째 줄은 'URL'일테니까

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    String str = row.getCell(urlColumn).getStringCellValue();
                    if(str.length() > 1)
                        urlList.add(str);
                }
                result.add(bundle);
            }
            
            xlsxWb.close();

            return result;

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}