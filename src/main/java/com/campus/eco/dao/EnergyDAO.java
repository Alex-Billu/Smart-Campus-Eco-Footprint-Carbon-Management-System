package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.EnergyUsage;
import com.campus.eco.service.CarbonFactors;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnergyDAO {

    public boolean insert(EnergyUsage e) throws SQLException {
        String sql = "INSERT INTO Energy_Usage (dept_id, usage_month, electricity_kwh, cost_amount, co2_emission_kg) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, e.getDeptId());
            ps.setString(2, e.getUsageMonth());
            ps.setBigDecimal(3, e.getElectricityKwh());
            ps.setBigDecimal(4, e.getCostAmount());
            ps.setBigDecimal(5, CarbonFactors.energyCo2(e.getElectricityKwh()));
            return ps.executeUpdate() > 0;
        }
    }

    public List<EnergyUsage> findAll() throws SQLException {
        List<EnergyUsage> list = new ArrayList<>();
        String sql = "SELECT * FROM Energy_Usage ORDER BY usage_month DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<EnergyUsage> findByDept(int deptId) throws SQLException {
        List<EnergyUsage> list = new ArrayList<>();
        String sql = "SELECT * FROM Energy_Usage WHERE dept_id=? ORDER BY usage_month DESC";
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
             PreparedStatement ps = c.prepareStatement("DELETE FROM Energy_Usage WHERE energy_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private EnergyUsage map(ResultSet rs) throws SQLException {
        EnergyUsage e = new EnergyUsage();
        e.setEnergyId(rs.getInt("energy_id"));
        e.setDeptId(rs.getInt("dept_id"));
        e.setUsageMonth(rs.getString("usage_month"));
        e.setElectricityKwh(rs.getBigDecimal("electricity_kwh"));
        e.setCostAmount(rs.getBigDecimal("cost_amount"));
        e.setCo2EmissionKg(rs.getBigDecimal("co2_emission_kg"));
        return e;
    }
}
