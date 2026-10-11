package com.campus.eco.service;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.AbstractMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChartServiceTest {

    @Test
    void pieChartIncludesProvidedValues() {
        JFreeChart chart = ChartService.pieChart("Emissions", Map.of("Energy", 12, "Water", 4));

        PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
        assertEquals("Emissions", chart.getTitle().getText());
        assertEquals(2, plot.getDataset().getItemCount());
        int energyIndex = plot.getDataset().getKeys().indexOf("Energy");
        assertEquals(12, plot.getDataset().getValue(energyIndex).intValue());
    }

    @Test
    void barChartCreatesCategoryValues() {
        JFreeChart chart = ChartService.barChart("By department", "Department", "CO2",
                Map.of("Science", 18.5, "Arts", 7.0));

        assertEquals(2, chart.getCategoryPlot().getDataset().getColumnCount());
        assertEquals(18.5, chart.getCategoryPlot().getDataset().getValue("CO2", "Science").doubleValue());
    }

    @Test
    void lineChartCreatesOrderedPoints() {
        JFreeChart chart = ChartService.lineChart("Trend", "Month", "CO2",
                List.of(new AbstractMap.SimpleEntry<>("Jan", 2), new AbstractMap.SimpleEntry<>("Feb", 5)));

        assertNotNull(chart.getXYPlot().getDataset());
        assertEquals(2, chart.getXYPlot().getDataset().getItemCount(0));
        assertEquals(5.0, chart.getXYPlot().getDataset().getYValue(0, 1));
    }

    @Test
    void multiLineChartCreatesOneSeriesPerEntry() {
        JFreeChart chart = ChartService.multiLine("Comparison", "Month", "CO2",
                Map.of("Energy", List.of(2, 3), "Water", List.of(5, 8)), List.of("Jan", "Feb"));

        assertEquals(2, chart.getXYPlot().getDataset().getSeriesCount());
        int waterIndex = "Water".equals(chart.getXYPlot().getDataset().getSeriesKey(0)) ? 0 : 1;
        assertEquals("Water", chart.getXYPlot().getDataset().getSeriesKey(waterIndex));
        assertEquals(8.0, chart.getXYPlot().getDataset().getYValue(waterIndex, 1));
    }
}