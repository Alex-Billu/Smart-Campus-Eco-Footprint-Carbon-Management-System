package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.CostAnalysis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CostAnalysisDAO {

    public boolean insert(CostAnalysis ca) throws SQLException {
        String sql = "INSERT INTO Cost_Analysis (dept_id, usage_month, energy_cost, water_cost, transport_cost, waste_cost, total_cost, total_co2, cost_per_co2) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (ca.getDeptId() == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, ca.getDeptId());
            ps.setString(2, ca.getUsageMonth());
            ps.setBigDecimal(3, ca.getEnergyCost());
            ps.setBigDecimal(4, ca.getWaterCost());
            ps.setBigDecimal(5, ca.getTransportCost());
            ps.setBigDecimal(6, ca.getWasteCost());
            ps.setBigDecimal(7, ca.getTotalCost());
            ps.setBigDecimal(8, ca.getTotalCo2());
            ps.setBigDecimal(9, ca.getCostPerCo2());
            return ps.executeUpdate() > 0;
        }
    }

    public List<CostAnalysis> findAll() throws SQLException {
        List<CostAnalysis> list = new ArrayList<>();
        String sql = "SELECT * FROM Cost_Analysis ORDER BY usage_month DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private CostAnalysis map(ResultSet rs) throws SQLException {
        CostAnalysis ca = new CostAnalysis();
        ca.setCostId(rs.getInt("cost_id"));
        int d = rs.getInt("dept_id");
        ca.setDeptId(rs.wasNull() ? null : d);
        ca.setUsageMonth(rs.getString("usage_month"));
        ca.setEnergyCost(rs.getBigDecimal("energy_cost"));
        ca.setWaterCost(rs.getBigDecimal("water_cost"));
        ca.setTransportCost(rs.getBigDecimal("transport_cost"));
        ca.setWasteCost(rs.getBigDecimal("waste_cost"));
        ca.setTotalCost(rs.getBigDecimal("total_cost"));
        ca.setTotalCo2(rs.getBigDecimal("total_co2"));
        ca.setCostPerCo2(rs.getBigDecimal("cost_per_co2"));
        return ca;
    }
}
