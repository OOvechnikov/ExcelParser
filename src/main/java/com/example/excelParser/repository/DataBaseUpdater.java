package com.example.excelParser.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

public interface DataBaseUpdater {

    @Transactional
    void saveDataToDb(String[] headCommonData, String[] headDynamicData, Object[][] bodyData) throws DataAccessException;
}
