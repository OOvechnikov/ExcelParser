package com.example.excelParser.mapper;

import com.example.excelParser.dto.ExDTO1;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExRowMapper1 implements RowMapper<ExDTO1> {

    @Override
    public ExDTO1 mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExDTO1 exDTO1 = new ExDTO1();
        exDTO1.setId(rs.getInt("id"));
        exDTO1.setDate(rs.getDate("date"));
        exDTO1.setCompany(rs.getString("company"));
        exDTO1.setCity(rs.getString("city"));
        exDTO1.setFactQliqData1(rs.getDouble("fact_Qliq_data1"));
        exDTO1.setFactQliqData2(rs.getDouble("fact_Qliq_data2"));
        exDTO1.setFactQoilData1(rs.getDouble("fact_Qoil_data1"));
        exDTO1.setFactQoilData2(rs.getDouble("fact_Qoil_data2"));
        exDTO1.setForecastQliqData1(rs.getDouble("forecast_Qliq_data1"));
        exDTO1.setForecastQliqData2(rs.getDouble("forecast_Qliq_data2"));
        exDTO1.setForecastQoilData1(rs.getDouble("forecast_Qoil_data1"));
        exDTO1.setForecastQoilData2(rs.getDouble("forecast_Qoil_data2"));
        return exDTO1;
    }
}
