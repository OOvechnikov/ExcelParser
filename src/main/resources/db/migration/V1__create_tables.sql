CREATE TABLE orders
(
    id INT8 PRIMARY KEY,
    date DATE NOT NULL,
    company VARCHAR(256) NOT NULL,
    fact_qliq_data1 NUMERIC(9, 2) NOT NULL,
    fact_qliq_data2 NUMERIC(9, 2) NOT NULL,
    fact_qoil_data1 NUMERIC(9, 2) NOT NULL,
    fact_qoil_data2 NUMERIC(9, 2) NOT NULL,
    forecast_qliq_data1 NUMERIC(9, 2) NOT NULL,
    forecast_qliq_data2 NUMERIC(9, 2) NOT NULL,
    forecast_qoil_data1 NUMERIC(9, 2) NOT NULL,
    forecast_qoil_data2 NUMERIC(9, 2) NOT NULL
);

CREATE TABLE total_by_day
(
    date DATE PRIMARY KEY,
    total_fact_qliq_data1 NUMERIC(9, 2) NOT NULL,
    total_fact_qliq_data2 NUMERIC(9, 2) NOT NULL,
    total_fact_qoil_data1 NUMERIC(9, 2) NOT NULL,
    total_fact_qoil_data2 NUMERIC(9, 2) NOT NULL,
    total_forecast_qliq_data1 NUMERIC(9, 2) NOT NULL,
    total_forecast_qliq_data2 NUMERIC(9, 2) NOT NULL,
    total_forecast_qoil_data1 NUMERIC(9, 2) NOT NULL,
    total_forecast_qoil_data2 NUMERIC(9, 2) NOT NULL
)
