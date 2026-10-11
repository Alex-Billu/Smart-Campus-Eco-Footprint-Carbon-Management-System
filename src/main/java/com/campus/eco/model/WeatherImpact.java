package com.campus.eco.model;

import java.math.BigDecimal;

public class WeatherImpact {
    private int weatherId;
    private String usageMonth;
    private BigDecimal temperatureC;
    private BigDecimal rainfallMm;
    private BigDecimal humidityPct;
    private String season;
    private String energyTrend;
    private String waterTrend;
    private String notes;

    public WeatherImpact() {}

    public int getWeatherId() { return weatherId; }
    public void setWeatherId(int weatherId) { this.weatherId = weatherId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public BigDecimal getTemperatureC() { return temperatureC; }
    public void setTemperatureC(BigDecimal temperatureC) { this.temperatureC = temperatureC; }
    public BigDecimal getRainfallMm() { return rainfallMm; }
    public void setRainfallMm(BigDecimal rainfallMm) { this.rainfallMm = rainfallMm; }
    public BigDecimal getHumidityPct() { return humidityPct; }
    public void setHumidityPct(BigDecimal humidityPct) { this.humidityPct = humidityPct; }
    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public String getEnergyTrend() { return energyTrend; }
    public void setEnergyTrend(String energyTrend) { this.energyTrend = energyTrend; }
    public String getWaterTrend() { return waterTrend; }
    public void setWaterTrend(String waterTrend) { this.waterTrend = waterTrend; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
