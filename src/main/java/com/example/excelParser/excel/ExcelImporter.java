package com.example.excelParser.excel;

import com.example.excelParser.exception.EmptyBookException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelImporter {

    private ExcelImporter() {
    }

    public static XSSFWorkbook importExcelFile(String path) throws EmptyBookException{
        XSSFWorkbook workbook;
        try (FileInputStream fis = new FileInputStream(path)) {
            workbook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            workbook = new XSSFWorkbook();
        }

        Iterator<Sheet> iterator = workbook.iterator();
        if (!iterator.hasNext()) {
            throw new EmptyBookException("Excel file doesn't exist or empty");
        }
        return workbook;
    }
}
