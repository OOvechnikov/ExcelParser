package com.example.excelParser.mapper;

import com.example.excelParser.dto.ExDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExRowMapper implements RowMapper<ExDTO> {

    @Override
    public ExDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExDTO exDTO = new ExDTO();
        exDTO.setId(rs.getInt("id"));
        exDTO.setDate(rs.getDate("date"));
        exDTO.setCompany(rs.getString("company"));
        exDTO.setFactQliqData1(rs.getDouble("fact_Qliq_data1"));
        exDTO.setFactQliqData2(rs.getDouble("fact_Qliq_data2"));
        exDTO.setFactQoilData1(rs.getDouble("fact_Qoil_data1"));
        exDTO.setFactQoilData2(rs.getDouble("fact_Qoil_data2"));
        exDTO.setForecastQliqData1(rs.getDouble("forecast_Qliq_data1"));
        exDTO.setForecastQliqData2(rs.getDouble("forecast_Qliq_data2"));
        exDTO.setForecastQoilData1(rs.getDouble("forecast_Qoil_data1"));
        exDTO.setForecastQoilData2(rs.getDouble("forecast_Qoil_data2"));
        return exDTO;
    }
}
