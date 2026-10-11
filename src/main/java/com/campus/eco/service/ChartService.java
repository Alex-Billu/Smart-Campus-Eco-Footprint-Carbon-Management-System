package com.campus.eco.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class ChartService {

    private static final Color GREEN = new Color(34, 139, 87);
    private static final Color BLUE = new Color(52, 118, 196);
    private static final Color ORANGE = new Color(232, 160, 40);

    public static JFreeChart pieChart(String title, Map<String, ? extends Number> data) {
        DefaultPieDataset<String> ds = new DefaultPieDataset<>();
        data.forEach((k, v) -> ds.setValue(k, v));
        JFreeChart chart = ChartFactory.createPieChart(title, ds, true, true, false);
        PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
        plot.setSectionPaint(0, GREEN);
        plot.setSectionPaint(1, BLUE);
        plot.setSectionPaint(2, ORANGE);
        plot.setSectionPaint(3, new Color(214, 64, 64));
        plot.setSectionPaint(4, new Color(150, 120, 200));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(new Color(245, 247, 244));
        return chart;
    }

    public static JFreeChart barChart(String title, String xLabel, String yLabel, Map<String, ? extends Number> data) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        data.forEach((k, v) -> ds.addValue(v, yLabel, k));
        JFreeChart chart = ChartFactory.createBarChart(title, xLabel, yLabel, ds, PlotOrientation.VERTICAL, false, true, false);
        chart.getCategoryPlot().getRenderer().setDefaultFillPaint(GREEN);
        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
        return chart;
    }

    public static JFreeChart lineChart(String title, String xLabel, String yLabel, List<? extends Map.Entry<String, ? extends Number>> series) {
        XYSeries s = new XYSeries(yLabel);
        int i = 1;
        for (Map.Entry<String, ? extends Number> e : series) { s.add(i++, e.getValue().doubleValue()); }
        XYSeriesCollection col = new XYSeriesCollection(s);
        JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, col, PlotOrientation.VERTICAL, false, true, false);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRenderer(new XYLineAndShapeRenderer(true, true));
        plot.getRenderer().setDefaultPaint(GREEN);
        plot.setAxisOffset(new RectangleInsets(0,0,0,0));
        return chart;
    }

    public static JFreeChart multiLine(String title, String xLabel, String yLabel, Map<String, List<? extends Number>> series, List<String> xLabels) {
        XYSeriesCollection col = new XYSeriesCollection();
        series.forEach((name, values) -> {
            XYSeries s = new XYSeries(name);
            for (int i = 0; i < values.size(); i++) s.add(i + 1, values.get(i).doubleValue());
            col.addSeries(s);
        });
        JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, col, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRenderer(new XYLineAndShapeRenderer(true, true));
        chart.setTitle(new TextTitle(title, new Font("Segoe UI", Font.BOLD, 14)));
        return chart;
    }
}
