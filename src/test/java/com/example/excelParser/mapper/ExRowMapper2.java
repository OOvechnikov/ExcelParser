package com.example.excelParser.mapper;

import com.example.excelParser.dto.ExDTO2;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExRowMapper2 implements RowMapper<ExDTO2> {

    @Override
    public ExDTO2 mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExDTO2 exDTO2 = new ExDTO2();
        exDTO2.setId(rs.getInt("id"));
        exDTO2.setDate(rs.getDate("date"));
        exDTO2.setCompany(rs.getString("company"));
        exDTO2.setFactQliqData1(rs.getDouble("fact_Qliq_data1"));
        exDTO2.setFactQliqData2(rs.getDouble("fact_Qliq_data2"));
        exDTO2.setFactQoilData1(rs.getDouble("fact_Qoil_data1"));
        exDTO2.setFactQoilData2(rs.getDouble("fact_Qoil_data2"));
        exDTO2.setForecastQliqData1(rs.getDouble("forecast_Qliq_data1"));
        exDTO2.setForecastQliqData2(rs.getDouble("forecast_Qliq_data2"));
        exDTO2.setForecastQoilData1(rs.getDouble("forecast_Qoil_data1"));
        exDTO2.setForecastQoilData2(rs.getDouble("forecast_Qoil_data2"));
        exDTO2.setFutureQnew1Data1(rs.getDouble("future_Qnew1_data1"));
        exDTO2.setFutureQnew2Data1(rs.getDouble("future_Qnew2_data1"));
        exDTO2.setFutureQnew2Data2(rs.getDouble("future_Qnew2_data2"));
        exDTO2.setFutureQnew2Data3(rs.getDouble("future_Qnew2_data3"));
        return exDTO2;
    }
}
