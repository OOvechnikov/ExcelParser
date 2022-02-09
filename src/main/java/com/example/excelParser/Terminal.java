package com.example.excelParser;

import com.example.excelParser.dbUpdater.DbUpdaterFactory;
import com.example.excelParser.dbUpdater.dateCreator.OneTwoThreeDateCreator;
import com.example.excelParser.excel.ExcelImporter;
import com.example.excelParser.excel.ParserFactory;
import com.example.excelParser.excel.ParserType;
import com.example.excelParser.excel.parsers.Parser;
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
        Parser parser = parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
        parser.configureWithValidation("company", "city", "id").parse(ExcelImporter.importExcelFile("src/main/resources/excel/Ex1.xlsx"));
        dbUpdaterFactory.build(parser, new OneTwoThreeDateCreator(2022, 0, 31)).saveDataToDb();
    }
}
