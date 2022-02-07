package com.example.excelParser.excel;

import com.example.excelParser.excel.parsers.CommonHeadWithDynamicDataParser;
import com.example.excelParser.excel.parsers.Parser;
import com.example.excelParser.exception.EmptyBookException;
import org.springframework.stereotype.Component;

@Component
public class ParserFactory {

    public Parser build(ParserType type) throws IllegalArgumentException, EmptyBookException {
        switch (type) {
            case COMMON_HEAD_WITH_DYNAMIC_DATA_PARSER: return new CommonHeadWithDynamicDataParser();

            default: throw new IllegalArgumentException(String.format("Parser with type %s doesn't exist", type));
        }
    }

}
