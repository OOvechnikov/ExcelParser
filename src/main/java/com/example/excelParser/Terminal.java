package com.example.excelParser;

import com.example.excelParser.excel.ExcelImporter;
import com.example.excelParser.excel.ParserFactory;
import com.example.excelParser.excel.ParserType;
import com.example.excelParser.excel.parsers.CommonHeadWithDynamicDataParser;
import com.example.excelParser.excel.parsers.Parser;
import com.example.excelParser.exception.EmptyBookException;
import com.example.excelParser.repository.ApplicationRepository;
import com.example.excelParser.repository.DataBaseUpdater;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Terminal implements CommandLineRunner {

    private final ParserFactory parserFactory;
    private final DataBaseUpdater dataBaseUpdater;


    @Autowired
    public Terminal(ParserFactory parserFactory, ApplicationRepository dataBaseUpdater) {
        this.parserFactory = parserFactory;
        this.dataBaseUpdater = dataBaseUpdater;
    }


    @Override
    public void run(String... args) {
//        XSSFWorkbook workbook = ExcelImporter.importExcelFile("src/test/resources/excel/Ex2.xlsx");
//        CommonHeadWithDynamicDataParser parser;
//        try {
//            parser = (CommonHeadWithDynamicDataParser) parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
//            parser.configureWithValidation(new String[] {"id", "date", "company"}, 2).parse(workbook);
//        } catch (IllegalArgumentException | EmptyBookException e) {
//            System.out.println(e.getMessage());
//            return;
//        }
//        dataBaseUpdater.saveDataToDb(parser.getHeadCommonData(), parser.getHeadDynamicData(), parser.getBodyData());
    }
}
