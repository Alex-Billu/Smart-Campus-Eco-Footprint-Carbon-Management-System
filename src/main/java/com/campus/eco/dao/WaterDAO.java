package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.WaterUsage;
import com.campus.eco.service.CarbonFactors;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WaterDAO {

    public boolean insert(WaterUsage w) throws SQLException {
        String sql = "INSERT INTO Water_Usage (dept_id, usage_month, water_litres, cost_amount, co2_emission_kg) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, w.getDeptId());
            ps.setString(2, w.getUsageMonth());
            ps.setBigDecimal(3, w.getWaterLitres());
            ps.setBigDecimal(4, w.getCostAmount());
            ps.setBigDecimal(5, CarbonFactors.waterCo2(w.getWaterLitres()));
            return ps.executeUpdate() > 0;
        }
    }

    public List<WaterUsage> findAll() throws SQLException {
        List<WaterUsage> list = new ArrayList<>();
        String sql = "SELECT * FROM Water_Usage ORDER BY usage_month DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<WaterUsage> findByDept(int deptId) throws SQLException {
        List<WaterUsage> list = new ArrayList<>();
        String sql = "SELECT * FROM Water_Usage WHERE dept_id=? ORDER BY usage_month DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Water_Usage WHERE water_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private WaterUsage map(ResultSet rs) throws SQLException {
        WaterUsage w = new WaterUsage();
        w.setWaterId(rs.getInt("water_id"));
        w.setDeptId(rs.getInt("dept_id"));
        w.setUsageMonth(rs.getString("usage_month"));
        w.setWaterLitres(rs.getBigDecimal("water_litres"));
        w.setCostAmount(rs.getBigDecimal("cost_amount"));
        w.setCo2EmissionKg(rs.getBigDecimal("co2_emission_kg"));
        return w;
    }
}
