package com.example.excelParser;

import com.example.excelParser.dbUpdater.DbUpdaterFactory;
import com.example.excelParser.excel.ExcelImporter;
import com.example.excelParser.excel.ParserFactory;
import com.example.excelParser.excel.ParserType;
import com.example.excelParser.excel.parsers.Parser;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Terminal implements CommandLineRunner {

    private final ParserFactory parserFactory;
    private final DbUpdaterFactory dbUpdaterFactory;


    @Autowired
    public Terminal(ParserFactory parserFactory, DbUpdaterFactory dbUpdaterFactory) {
        this.parserFactory = parserFactory;
        this.dbUpdaterFactory = dbUpdaterFactory;
    }


    @Override
    public void run(String... args) {
//        XSSFWorkbook workbook = ExcelImporter.importExcelFile("src/test/resources/excel/Ex2.xlsx");
//        Parser parser = parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
//        parser.configureWithValidation(new String[] {"id", "date" , "company"}, 2).parse(workbook);
//        dbUpdaterFactory.build(parser).saveDataToDb();
    }
}
