package com.example.excelParser;

import com.example.excelParser.dto.ExDTO;
import com.example.excelParser.dto.ExDTO1;
import com.example.excelParser.dto.ExDTO2;
import com.example.excelParser.excel.ExcelImporter;
import com.example.excelParser.excel.ParserFactory;
import com.example.excelParser.excel.ParserType;
import com.example.excelParser.excel.parsers.CommonHeadWithDynamicDataParser;
import com.example.excelParser.exception.BeforeParseException;
import com.example.excelParser.exception.EmptyBookException;
import com.example.excelParser.mapper.ExRowMapper;
import com.example.excelParser.mapper.ExRowMapper1;
import com.example.excelParser.mapper.ExRowMapper2;
import com.example.excelParser.repository.DataBaseUpdater;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class ApplicationTest {

    private final JdbcTemplate jdbcTemplate;
    private final ParserFactory parserFactory;
    private final DataBaseUpdater dataBaseUpdater;

    @Autowired
    public ApplicationTest(JdbcTemplate jdbcTemplate, ParserFactory parserFactory, DataBaseUpdater dataBaseUpdater) {
        this.jdbcTemplate = jdbcTemplate;
        this.parserFactory = parserFactory;
        this.dataBaseUpdater = dataBaseUpdater;
    }

    @AfterEach
    void tearDown() {
        String sql = "DROP TABLE IF EXISTS orders, total_by_day";
        jdbcTemplate.update(sql);
    }


    @Test
    void workbookNotExistTest() {
        EmptyBookException ex = assertThrows(EmptyBookException.class,
                () -> ExcelImporter.importExcelFile("src/test/resources/excel/NotExist.xlsx"));
        assertEquals("Excel file doesn't exist or empty", ex.getMessage());
    }

    @Test
    void wrongArgumentsOrderToParserTest() {
        XSSFWorkbook workbook = ExcelImporter.importExcelFile("src/test/resources/excel/Ex.xlsx");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER)
                        .configureWithValidation(2, new String[] {}).parse(workbook));
        assertEquals("Args must be String[] (table head data) and int (date column number)", ex.getMessage());
    }

    @Test
    void argumentsWithoutIdAndDateToParserTest() {
        XSSFWorkbook workbook = ExcelImporter.importExcelFile("src/test/resources/excel/Ex.xlsx");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER)
                        .configureWithValidation(new String[] {"one", "two", "three"}, 2).parse(workbook));
        assertEquals("Table head data must contains 'id' and 'date' columns; date column number must be > 0", ex.getMessage());
    }

    @Test
    void getDataBeforeParseTest() {
        BeforeParseException ex = assertThrows(BeforeParseException.class,
                () -> {
                    CommonHeadWithDynamicDataParser parser =
                            (CommonHeadWithDynamicDataParser) parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
                    dataBaseUpdater.saveDataToDb(parser.getHeadCommonData(), parser.getHeadDynamicData(), parser.getBodyData());
                });

        assertEquals("Must to call 'configure' and 'parse' methods before", ex.getMessage());
    }

    @Test
    void givenFileTest() {
        XSSFWorkbook workbook = ExcelImporter.importExcelFile("src/test/resources/excel/Ex.xlsx");
        CommonHeadWithDynamicDataParser parser;
        try {
            parser = (CommonHeadWithDynamicDataParser) parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
            parser.configureWithValidation(new String[] {"id", "date", "company"}, 2).parse(workbook);
        } catch (IllegalArgumentException | EmptyBookException e) {
            System.out.println(e.getMessage());
            return;
        }
        dataBaseUpdater.saveDataToDb(parser.getHeadCommonData(), parser.getHeadDynamicData(), parser.getBodyData());

        String sql = "SELECT * FROM orders";
        List<ExDTO> list = jdbcTemplate.query(sql, new ExRowMapper());
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertThat(list.size() == 20);
        ExDTO ex10 = list.get(9);
        assertThat(ex10.getForecastQliqData1() == (double) 21);
    }

    @Test
    void extendedHeadDataTest() {
        XSSFWorkbook workbook = ExcelImporter.importExcelFile("src/test/resources/excel/Ex2.xlsx");
        CommonHeadWithDynamicDataParser parser;
        try {
            parser = (CommonHeadWithDynamicDataParser) parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
            parser.configureWithValidation(new String[] {"id", "date", "company"}, 2).parse(workbook);
        } catch (IllegalArgumentException | EmptyBookException e) {
            System.out.println(e.getMessage());
            return;
        }
        dataBaseUpdater.saveDataToDb(parser.getHeadCommonData(), parser.getHeadDynamicData(), parser.getBodyData());

        String sql = "SELECT * FROM orders";
        List<ExDTO2> list = jdbcTemplate.query(sql, new ExRowMapper2());
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertThat(list.size() == 20);
        ExDTO2 ex18 = list.get(18);
        assertThat(ex18.getFactQoilData2() == (double) 58);
        assertThat(ex18.getFutureQnew2Data2() == (double) 807);
    }

    @Test
    void extendedCommonHeadTest() {
        XSSFWorkbook workbook = ExcelImporter.importExcelFile("src/test/resources/excel/Ex1.xlsx");
        CommonHeadWithDynamicDataParser parser;
        try {
            parser = (CommonHeadWithDynamicDataParser) parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
            parser.configureWithValidation(new String[] {"id", "date", "company", "city"}, 2).parse(workbook);
        } catch (IllegalArgumentException | EmptyBookException e) {
            System.out.println(e.getMessage());
            return;
        }
        dataBaseUpdater.saveDataToDb(parser.getHeadCommonData(), parser.getHeadDynamicData(), parser.getBodyData());

        String sql = "SELECT * FROM orders";
        List<ExDTO1> list = jdbcTemplate.query(sql, new ExRowMapper1());
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertThat(list.size() == 25);
        ExDTO1 ex18 = list.get(23);
        assertThat(ex18.getFactQoilData2() == (double) 63);
        assertThat(ex18.getForecastQoilData1() == (double) 130);
    }
}