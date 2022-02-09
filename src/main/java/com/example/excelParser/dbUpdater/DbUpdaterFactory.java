package com.example.excelParser.dbUpdater;

import com.example.excelParser.dbUpdater.dateCreator.DateCreator;
import com.example.excelParser.excel.parsers.CommonHeadWithDynamicDataParser;
import com.example.excelParser.excel.parsers.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbUpdaterFactory {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUpdaterFactory(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DataBaseUpdater build(Parser parser, DateCreator dateCreator) {
        switch (parser.getType()) {
            case COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER: return new CommonHeadWithDynamicDataParserDataBaseUpdater(jdbcTemplate, (CommonHeadWithDynamicDataParser) parser, dateCreator);

            default: throw new IllegalArgumentException(String.format("Parser with type %s doesn't exist", parser.getType()));
        }
    }
}
