package com.example.excelParser.dbUpdater;

import com.example.excelParser.excel.parsers.CommonHeadWithDynamicDataParser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonHeadWithDynamicDataParserDataBaseUpdater implements DataBaseUpdater {

    private static final String MAIN_TABLE_NAME = "orders";
    private static final String TOTAL_TABLE_NAME = "total_by_day";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private static final StringBuilder SB_MAIN = new StringBuilder();
    private static final StringBuilder SB_HELPER = new StringBuilder();

    private final JdbcTemplate jdbcTemplate;
    private final CommonHeadWithDynamicDataParser commonHeadWithDynamicDataParser;

    public CommonHeadWithDynamicDataParserDataBaseUpdater(JdbcTemplate jdbcTemplate, CommonHeadWithDynamicDataParser commonHeadWithDynamicDataParser) {
        this.jdbcTemplate = jdbcTemplate;
        this.commonHeadWithDynamicDataParser = commonHeadWithDynamicDataParser;
    }

    @Override
    @Transactional
    public void saveDataToDb() throws DataAccessException {
        createTables(commonHeadWithDynamicDataParser.getHeadCommonData(), commonHeadWithDynamicDataParser.getHeadDynamicData());
        fillTables(commonHeadWithDynamicDataParser.getHeadDynamicData(), commonHeadWithDynamicDataParser.getBodyData());
    }


    private void createTables(String[] headCommonData, String[] headDynamicData) throws DataAccessException {
        String sql =
                "CREATE TABLE " + MAIN_TABLE_NAME + " (" +
                sqlMainHeadCommonPart(headCommonData) + " " +
                sqlMainHeadDynamicPart(headDynamicData) +
                ")";
        jdbcTemplate.execute(sql);

        sql =
                "CREATE TABLE " + TOTAL_TABLE_NAME + " (" +
                "date DATE PRIMARY KEY, " +
                sqlTotalHeadDynamicPart(headDynamicData) +
                ")";
        jdbcTemplate.execute(sql);
    }

    private void fillTables(String[] headDynamicData, Object[][] bodyData) throws DataAccessException {
        String sql =
                "INSERT INTO " + MAIN_TABLE_NAME + " " +
                "VALUES " + sqlMainBodyValues(bodyData);
        jdbcTemplate.update(sql);

        sql =
                "INSERT INTO " + TOTAL_TABLE_NAME + " " +
                "SELECT date, " + sqlTotalBodyValues(headDynamicData) + " " +
                "FROM " + MAIN_TABLE_NAME + " " +
                "GROUP BY date";
        jdbcTemplate.update(sql);
    }

    private String sqlMainHeadCommonPart(String[] headCommonData) {
        SB_MAIN.delete(0, SB_MAIN.length());
        for (String s : headCommonData) {
            if (s.equals("id")) {
                SB_MAIN.append("id INT8 PRIMARY KEY, ");
            } else if (s.equals("date")) {
                SB_MAIN.append("date DATE NOT NULL, ");
            } else {
                SB_MAIN.append(s).append(" VARCHAR(256) NOT NULL, ");
            }
        }
        return SB_MAIN.toString();
    }

    private String sqlMainHeadDynamicPart(String[] headDynamicData) {
        SB_MAIN.delete(0, SB_MAIN.length());
        for (String s : headDynamicData) {
            SB_MAIN.append(s).append(" NUMERIC(9, 2) NOT NULL, ");
        }
        return SB_MAIN.delete(SB_MAIN.length() - 2, SB_MAIN.length()).toString();
    }

    private String sqlTotalHeadDynamicPart(String[] headDynamicData) {
        SB_MAIN.delete(0, SB_MAIN.length());
        for (String s : headDynamicData) {
            s = "total_" + s;
            SB_MAIN.append(s).append(" NUMERIC(9, 2) NOT NULL, ");
        }
        return SB_MAIN.delete(SB_MAIN.length() - 2, SB_MAIN.length()).toString();
    }

    private String sqlMainBodyValues(Object[][] bodyData) {
        SB_MAIN.delete(0, SB_MAIN.length());
        for (int i = 0; i < bodyData.length; i++) {
            SB_MAIN.append("(").append(getValuesAsStringFromArray(bodyData, i)).append("), ");
        }
        return SB_MAIN.delete(SB_MAIN.length() - 2, SB_MAIN.length()).toString();
    }

    private String getValuesAsStringFromArray(Object[][] data, int i) {
        SB_HELPER.delete(0, SB_HELPER.length());
        for (int j = 0; j < data[i].length; j++) {
            SB_HELPER.append(convertDataToString(data[i][j])).append(", ");
        }
        return SB_HELPER.delete(SB_HELPER.length() - 2, SB_HELPER.length()).toString();
    }

    private String sqlTotalBodyValues(String[] dynamicPart) {
        SB_MAIN.delete(0, SB_MAIN.length());
        for (String s : dynamicPart) {
            SB_MAIN.append("SUM(").append(s).append(") AS total_").append(", ");
        }
        return SB_MAIN.delete(SB_MAIN.length() - 2, SB_MAIN.length()).toString();
    }

    private String convertDataToString(Object obj) {
        if (obj instanceof Date) {
            return "'" + SDF.format(obj) + "'";
        } else if (obj instanceof String) {
            return "'" + obj + "'";
        }
        return obj.toString();
    }
}
