package com.example.excelParser.excel.parsers;

import com.example.excelParser.excel.ParserType;
import com.example.excelParser.exception.BeforeParseException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class Parser {
    ParserType type;
    int headRowQty;
    int bodyRowQty;
    int tableColumnQty;

    Object[][] headData;
    Object[][] bodyData;

    XSSFWorkbook workbook;
    Sheet sheet;

    public Parser(ParserType type) {
        this.type = type;
    }

    public abstract void parse(XSSFWorkbook workbook);
    public abstract Parser configureWithValidation(Object... args) throws IllegalArgumentException;
    public abstract Object[][] getBodyData() throws BeforeParseException;
    public abstract Object[][] getHeadData() throws BeforeParseException;

    public ParserType getType() {
        return type;
    }
}
