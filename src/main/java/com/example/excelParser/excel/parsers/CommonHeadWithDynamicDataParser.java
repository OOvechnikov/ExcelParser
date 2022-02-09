package com.example.excelParser.excel.parsers;

import com.example.excelParser.excel.ParserType;
import com.example.excelParser.exception.BeforeParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Arrays;

public class CommonHeadWithDynamicDataParser extends Parser {
    private static final String ID = "id";
    private static final String BPE_MESSAGE = "Must to call 'configure' and 'parse' methods before";

    private String[] headCommonData;
    private String[] headDynamicData;
    private int idColumnNumber;

    public CommonHeadWithDynamicDataParser(ParserType type) {
        super(type);
    }


    @Override
    public void parse(XSSFWorkbook workbook) {
        this.workbook = workbook;
        sheet = workbook.getSheetAt(0);
        headRowQty = findHeadRowQty();
        tableColumnQty = findTableColumnQty();
        bodyRowQty = findBodyRowQty();
        headDynamicData = setHeadDynamicData();
        headData = setHeadData();
        bodyData = setBodyData();
    }

    @Override
    public CommonHeadWithDynamicDataParser configureWithValidation(Object... args) throws IllegalArgumentException {
        boolean isArgsStrings = Arrays.stream(args).allMatch(a -> a instanceof String);
        if (!isArgsStrings) {
            throw new IllegalArgumentException("All arguments must be String. Describe table common head data");
        } else if (Arrays.stream(args).filter(s -> s.equals(ID)).count() != 1) {
            throw new IllegalArgumentException(String.format("Table common head data must contains one '%s' column", ID));
        }
        headCommonData = Arrays.asList(args).toArray(new String[args.length]);
        for (int i = 0; i < headCommonData.length; i++) {
            if (headCommonData[i].equals("id")) {
                idColumnNumber = i;
                break;
            }
        }
        return this;
    }

    public String[] getHeadCommonData() throws BeforeParseException {
        if (headCommonData == null) {
            throw new BeforeParseException(BPE_MESSAGE);
        }
        return headCommonData;
    }

    public String[] getHeadDynamicData() throws BeforeParseException {
        if (headDynamicData == null) {
            throw new BeforeParseException(BPE_MESSAGE);
        }
        return headDynamicData;
    }

    @Override
    public Object[][] getBodyData() throws BeforeParseException {
        if (bodyData == null) {
            throw new BeforeParseException(BPE_MESSAGE);
        }
        return bodyData;
    }

    @Override
    public Object[][] getHeadData() throws BeforeParseException {
        if (headData == null) {
            throw new BeforeParseException(BPE_MESSAGE);
        }
        return headData;
    }


    private String[] setHeadDynamicData() {
        String[] data = new String[tableColumnQty - headCommonData.length];
        for (int i = headRowQty - 1; i >= 0; i--) {
            Row row = sheet.getRow(i);
            String concatValue = "";
            for (int j = headCommonData.length; j < tableColumnQty; j++) {
                Cell cell = row.getCell(j);
                if (cell.getCellType() != CellType.BLANK) {
                    concatValue = "_" + cell.getStringCellValue();
                }
                data[j - headCommonData.length] = concatValue + data[j - headCommonData.length];
            }
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].replace("null", "");
            data[i] = data[i].substring(1);
        }
        return data;
    }

    private Object[][] setBodyData() {
        Object[][] data = new Object[bodyRowQty][tableColumnQty];
        for (int i = headRowQty; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < tableColumnQty; j++) {
                Cell cell = row.getCell(j);
                if (j < headCommonData.length) {
                    if (j == idColumnNumber) {
                        data[i - headRowQty][j] = cell.getNumericCellValue();
                        continue;
                    }
                    data[i - headRowQty][j] = cell.getStringCellValue();
                } else {
                    data[i - headRowQty][j] = cell.getNumericCellValue();
                }
            }
        }
        return data;
    }

    private Object[][] setHeadData() {
        Object[][] data = new Object[headRowQty][tableColumnQty];
        for (int i = 0; i < headRowQty; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < tableColumnQty; j++) {
                Cell cell = row.getCell(j);
                data[i][j] = cell.getStringCellValue();
            }
        }
        return data;
    }

    private CellRangeAddress getMergedRegion(Cell cell) {
        for (CellRangeAddress range : sheet.getMergedRegions()) {
            if (range.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                return range;
            }
        }
        return null;
    }

    private int findHeadRowQty() {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getStringCellValue().equals(ID)) {
                    CellRangeAddress range = getMergedRegion(cell);
                    if (range == null) {
                        return 1;
                    }
                    return range.getLastRow() - range.getFirstRow() + 1;
                }
            }
        }
        return -1;
    }

    private int findTableColumnQty() {
        Row lastHeadRow = sheet.getRow(headRowQty - 1);
        for (int i = headCommonData.length; i < Integer.MAX_VALUE; i++) {
            if (lastHeadRow.getCell(i) == null || lastHeadRow.getCell(i).getCellType() == CellType.BLANK) {
                return i;
            }
        }
        return -1;
    }

    private int findBodyRowQty() {
        return sheet.getLastRowNum() + 1 - headRowQty;
    }
}
