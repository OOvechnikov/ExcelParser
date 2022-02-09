package com.example.excelParser;

import com.example.excelParser.dbUpdater.DbUpdaterFactory;
import com.example.excelParser.dbUpdater.dateCreator.OneTwoThreeDateCreator;
import com.example.excelParser.dto.ExDTO;
import com.example.excelParser.dto.ExDTO1;
import com.example.excelParser.dto.ExDTO2;
import com.example.excelParser.excel.ExcelImporter;
import com.example.excelParser.excel.ParserFactory;
import com.example.excelParser.excel.ParserType;
import com.example.excelParser.excel.parsers.Parser;
import com.example.excelParser.exception.BeforeParseException;
import com.example.excelParser.exception.EmptyBookException;
import com.example.excelParser.mapper.ExRowMapper;
import com.example.excelParser.mapper.ExRowMapper1;
import com.example.excelParser.mapper.ExRowMapper2;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class ApplicationTest {

    private final ParserFactory parserFactory;
    private final DbUpdaterFactory dbUpdaterFactory;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public ApplicationTest(ParserFactory parserFactory, DbUpdaterFactory dbUpdaterFactory, JdbcTemplate jdbcTemplate) {
        this.parserFactory = parserFactory;
        this.dbUpdaterFactory = dbUpdaterFactory;
        this.jdbcTemplate = jdbcTemplate;
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
    void wrongArgumentsToParserTest() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER)
                        .configureWithValidation("id", "company", 35));
        assertEquals("All arguments must be String. Describe table common head data", ex.getMessage());
    }

    @Test
    void argumentsWithoutIdParserTest() {
        XSSFWorkbook workbook = ExcelImporter.importExcelFile("src/test/resources/excel/Ex.xlsx");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER)
                        .configureWithValidation("one", "two", "three").parse(workbook));
        assertEquals("Table common head data must contains one 'id' column", ex.getMessage());
        ex = assertThrows(IllegalArgumentException.class,
                () -> parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER)
                        .configureWithValidation("id", "two", "id").parse(workbook));
        assertEquals("Table common head data must contains one 'id' column", ex.getMessage());
    }

    @Test
    void getDataBeforeParseTest() {
        BeforeParseException ex = assertThrows(BeforeParseException.class,
                () -> {
                    Parser parser = parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
                    dbUpdaterFactory.build(parser, new OneTwoThreeDateCreator(2022, 1, 28)).saveDataToDb();
                });
        assertEquals("Must to call 'configure' and 'parse' methods before", ex.getMessage());
    }

    @Test
    void givenFileTest() {
        XSSFWorkbook workbook = ExcelImporter.importExcelFile("src/test/resources/excel/Ex.xlsx");
        Parser parser = parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
        parser.configureWithValidation("id", "company").parse(workbook);
        dbUpdaterFactory.build(parser, new OneTwoThreeDateCreator(2022, 1, 28)).saveDataToDb();

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
        Parser parser = parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
        parser.configureWithValidation("id", "company").parse(workbook);
        dbUpdaterFactory.build(parser, new OneTwoThreeDateCreator(2022, 1, 28)).saveDataToDb();

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
        Parser parser = parserFactory.build(ParserType.COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER);
        parser.configureWithValidation("company", "city", "id").parse(workbook);
        dbUpdaterFactory.build(parser, new OneTwoThreeDateCreator(2022, 1, 28)).saveDataToDb();

        String sql = "SELECT * FROM orders";
        List<ExDTO1> list = jdbcTemplate.query(sql, new ExRowMapper1());
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertThat(list.size() == 25);
        ExDTO1 ex18 = list.get(23);
        assertThat(ex18.getFactQoilData2() == (double) 63);
        assertThat(ex18.getForecastQoilData1() == (double) 130);
    }

    @Test
    void OneTwoThreeDateCreatorTest() {
        OneTwoThreeDateCreator creator = new OneTwoThreeDateCreator(2022, Calendar.FEBRUARY, 9);
        assertEquals(new Date(122, 1, 9), creator.createDate());
        assertEquals(new Date(122, 1, 10), creator.createDate());
        assertEquals(new Date(122, 1, 11), creator.createDate());
        assertEquals(new Date(122, 1, 9), creator.createDate());

        creator = new OneTwoThreeDateCreator(2022, Calendar.FEBRUARY, 28);
        assertEquals(new Date(122, 1, 28), creator.createDate());
        assertEquals(new Date(122, 1, 1), creator.createDate());
        assertEquals(new Date(122, 1, 2), creator.createDate());
        assertEquals(new Date(122, 1, 28), creator.createDate());

        creator = new OneTwoThreeDateCreator(2021, Calendar.DECEMBER, 31);
        assertEquals(new Date(121, 11, 31), creator.createDate());
        assertEquals(new Date(121, 11, 1), creator.createDate());
        assertEquals(new Date(121, 11, 2), creator.createDate());
        assertEquals(new Date(121, 11, 31), creator.createDate());
    }
}