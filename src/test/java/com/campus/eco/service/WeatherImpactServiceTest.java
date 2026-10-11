package com.campus.eco.service;

import com.campus.eco.model.WeatherImpact;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherImpactServiceTest {

    private final WeatherImpactService service = new WeatherImpactService();

    @Test
    void analyzeClassifiesSeasonAndWeatherTrends() {
        WeatherImpact impact = service.analyze("2026-04", new BigDecimal("31"),
                new BigDecimal("151"), new BigDecimal("60"));

        assertEquals("Summer", impact.getSeason());
        assertEquals("High (cooling load)", impact.getEnergyTrend());
        assertEquals("Lower (rainwater)", impact.getWaterTrend());
        assertEquals("2026-04", impact.getUsageMonth());
        assertEquals(new BigDecimal("60"), impact.getHumidityPct());
    }

    @Test
    void analyzeUsesNormalTrendsAtThresholds() {
        WeatherImpact impact = service.analyze("01", new BigDecimal("30"),
                new BigDecimal("150"), BigDecimal.ZERO);

        assertEquals("Winter", impact.getSeason());
        assertEquals("Normal", impact.getEnergyTrend());
        assertEquals("Normal", impact.getWaterTrend());
    }

    @Test
    void analyzeHandlesNullAndShortMonthValues() {
        assertEquals("Unknown", service.analyze(null, null, null, null).getSeason());
        assertEquals("Autumn", service.analyze("10", null, null, null).getSeason());
        assertEquals("Monsoon", service.analyze("2026-09", null, null, null).getSeason());
    }

    @Test
    void forecastReportsInsufficientDataForEmptyHistory() {
        assertEquals("Insufficient data for forecast.", service.forecastSummerEnergy(List.of()));
    }

    @Test
    void forecastUsesAvailableTemperaturesAndIgnoresMissingValues() {
        WeatherImpact warm = new WeatherImpact();
        warm.setTemperatureC(new BigDecimal("20"));
        WeatherImpact hot = new WeatherImpact();
        hot.setTemperatureC(new BigDecimal("30"));
        WeatherImpact missing = new WeatherImpact();

        assertEquals("Predicted summer electricity: ~1500.00 kWh/dept (based on avg temp 25.0C).",
                service.forecastSummerEnergy(List.of(warm, hot, missing)));
    }

    @Test
    void forecastUsesDefaultTemperatureWhenHistoryHasNoTemperatures() {
        assertEquals("Predicted summer electricity: ~1500.00 kWh/dept (based on avg temp 25.0C).",
                service.forecastSummerEnergy(List.of(new WeatherImpact())));
    }
}