package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.WeatherImpact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeatherImpactDAO {

    public boolean insert(WeatherImpact w) throws SQLException {
        String sql = "INSERT INTO Weather_Impact (usage_month, temperature_c, rainfall_mm, humidity_pct, season, energy_trend, water_trend, notes) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, w.getUsageMonth());
            ps.setBigDecimal(2, w.getTemperatureC());
            ps.setBigDecimal(3, w.getRainfallMm());
            ps.setBigDecimal(4, w.getHumidityPct());
            ps.setString(5, w.getSeason());
            ps.setString(6, w.getEnergyTrend());
            ps.setString(7, w.getWaterTrend());
            ps.setString(8, w.getNotes());
            return ps.executeUpdate() > 0;
        }
    }

    public List<WeatherImpact> findAll() throws SQLException {
        List<WeatherImpact> list = new ArrayList<>();
        String sql = "SELECT * FROM Weather_Impact ORDER BY usage_month DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private WeatherImpact map(ResultSet rs) throws SQLException {
        WeatherImpact w = new WeatherImpact();
        w.setWeatherId(rs.getInt("weather_id"));
        w.setUsageMonth(rs.getString("usage_month"));
        w.setTemperatureC(rs.getBigDecimal("temperature_c"));
        w.setRainfallMm(rs.getBigDecimal("rainfall_mm"));
        w.setHumidityPct(rs.getBigDecimal("humidity_pct"));
        w.setSeason(rs.getString("season"));
        w.setEnergyTrend(rs.getString("energy_trend"));
        w.setWaterTrend(rs.getString("water_trend"));
        w.setNotes(rs.getString("notes"));
        return w;
    }
}
