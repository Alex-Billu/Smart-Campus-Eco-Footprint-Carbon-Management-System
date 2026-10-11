package com.campus.eco.service;

import com.campus.eco.model.WeatherImpact;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Weather impact analysis - derives trends from temperature/rainfall.
 */
import com.campus.eco.dao.WeatherImpactDAO;
public class WeatherImpactService {

    private final WeatherImpactDAO dao = new WeatherImpactDAO();

    public WeatherImpact analyze(String month, BigDecimal temp, BigDecimal rainfall, BigDecimal humidity) {
        WeatherImpact w = new WeatherImpact();
        w.setUsageMonth(month);
        w.setTemperatureC(temp);
        w.setRainfallMm(rainfall);
        w.setHumidityPct(humidity);
        w.setSeason(seasonFor(month));
        w.setEnergyTrend(temp != null && temp.compareTo(new BigDecimal("30")) > 0 ? "High (cooling load)" : "Normal");
        w.setWaterTrend(rainfall != null && rainfall.compareTo(new BigDecimal("150")) > 0 ? "Lower (rainwater)" : "Normal");
        w.setNotes("Derived from temperature " + temp + "C, rainfall " + rainfall + "mm, humidity " + humidity + "%");
        return w;
    }

    public boolean persist(WeatherImpact w) throws java.sql.SQLException { return dao.insert(w); }
    public List<WeatherImpact> all() throws java.sql.SQLException { return dao.findAll(); }

    public String forecastSummerEnergy(List<WeatherImpact> history) {
        if (history.isEmpty()) return "Insufficient data for forecast.";
        double avgTemp = history.stream().filter(h -> h.getTemperatureC() != null).mapToDouble(h -> h.getTemperatureC().doubleValue()).average().orElse(25);
        BigDecimal predicted = BigDecimal.valueOf(avgTemp * 60).setScale(2, RoundingMode.HALF_UP);
        return "Predicted summer electricity: ~" + predicted + " kWh/dept (based on avg temp " + avgTemp + "C).";
    }

    private String seasonFor(String month) {
        if (month == null) return "Unknown";
        String m = month.length() >= 7 ? month.substring(5, 7) : month;
        switch (m) {
            case "12": case "01": case "02": return "Winter";
            case "03": case "04": case "05": return "Summer";
            case "06": case "07": case "08": case "09": return "Monsoon";
            default: return "Autumn";
        }
    }
}
