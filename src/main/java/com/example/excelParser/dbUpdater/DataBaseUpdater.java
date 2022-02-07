package com.example.excelParser.dbUpdater;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

public interface DataBaseUpdater {

    @Transactional
    void saveDataToDb() throws DataAccessException;
}
