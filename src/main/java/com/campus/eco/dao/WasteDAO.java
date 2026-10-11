package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.WasteRecord;
import com.campus.eco.service.CarbonFactors;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WasteDAO {

    public boolean insert(WasteRecord w) throws SQLException {
        String sql = "INSERT INTO Waste_Record (dept_id, usage_month, recyclable_kg, non_recyclable_kg, co2_emission_kg) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, w.getDeptId());
            ps.setString(2, w.getUsageMonth());
            ps.setBigDecimal(3, w.getRecyclableKg());
            ps.setBigDecimal(4, w.getNonRecyclableKg());
            ps.setBigDecimal(5, CarbonFactors.wasteCo2(w.getRecyclableKg(), w.getNonRecyclableKg()));
            return ps.executeUpdate() > 0;
        }
    }

    public List<WasteRecord> findAll() throws SQLException {
        List<WasteRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM Waste_Record ORDER BY usage_month DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<WasteRecord> findByDept(int deptId) throws SQLException {
        List<WasteRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM Waste_Record WHERE dept_id=? ORDER BY usage_month DESC";
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
             PreparedStatement ps = c.prepareStatement("DELETE FROM Waste_Record WHERE waste_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private WasteRecord map(ResultSet rs) throws SQLException {
        WasteRecord w = new WasteRecord();
        w.setWasteId(rs.getInt("waste_id"));
        w.setDeptId(rs.getInt("dept_id"));
        w.setUsageMonth(rs.getString("usage_month"));
        w.setRecyclableKg(rs.getBigDecimal("recyclable_kg"));
        w.setNonRecyclableKg(rs.getBigDecimal("non_recyclable_kg"));
        w.setCo2EmissionKg(rs.getBigDecimal("co2_emission_kg"));
        return w;
    }
}
