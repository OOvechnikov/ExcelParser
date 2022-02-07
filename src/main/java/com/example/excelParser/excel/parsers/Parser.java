package com.example.excelParser.excel.parsers;

import com.example.excelParser.exception.BeforeParseException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class Parser {
    int headRowQty;
    int bodyRowQty;
    int tableColumnQty;

    Object[][] headData;
    Object[][] bodyData;

    XSSFWorkbook workbook;
    Sheet sheet;

    public abstract void parse(XSSFWorkbook workbook);
    public abstract Parser configureWithValidation(Object... args) throws IllegalArgumentException;
    public abstract Object[][] getBodyData() throws BeforeParseException;
    public abstract Object[][] getHeadData() throws BeforeParseException;
}
