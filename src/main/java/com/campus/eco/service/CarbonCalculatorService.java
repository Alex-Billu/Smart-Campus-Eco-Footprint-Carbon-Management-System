package com.campus.eco.service;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.CarbonReport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarbonCalculatorService {

    /** Aggregate per-department carbon for a given month from all source tables and persist into Carbon_Report. */
    public List<CarbonReport> generateForMonth(String month) throws SQLException {
        Map<Integer, CarbonReport> map = new HashMap<>();
        try (Connection c = DBConnection.getConnection()) {
            // Energy
            try (PreparedStatement ps = c.prepareStatement("SELECT dept_id, COALESCE(SUM(co2_emission_kg),0) s FROM Energy_Usage WHERE usage_month=? GROUP BY dept_id")) {
                ps.setString(1, month);
                try (ResultSet rs = ps.executeQuery()) { while (rs.next()) getOrCreate(map, rs.getInt("dept_id"), month).setEnergyCo2(rs.getBigDecimal("s")); }
            }
            // Water
            try (PreparedStatement ps = c.prepareStatement("SELECT dept_id, COALESCE(SUM(co2_emission_kg),0) s FROM Water_Usage WHERE usage_month=? GROUP BY dept_id")) {
                ps.setString(1, month);
                try (ResultSet rs = ps.executeQuery()) { while (rs.next()) getOrCreate(map, rs.getInt("dept_id"), month).setWaterCo2(rs.getBigDecimal("s")); }
            }
            // Transport
            try (PreparedStatement ps = c.prepareStatement("SELECT dept_id, COALESCE(SUM(co2_emission_kg),0) s FROM Transport_Details WHERE usage_month=? GROUP BY dept_id")) {
                ps.setString(1, month);
                try (ResultSet rs = ps.executeQuery()) { while (rs.next()) getOrCreate(map, rs.getInt("dept_id"), month).setTransportCo2(rs.getBigDecimal("s")); }
            }
            // Waste
            try (PreparedStatement ps = c.prepareStatement("SELECT dept_id, COALESCE(SUM(co2_emission_kg),0) s FROM Waste_Record WHERE usage_month=? GROUP BY dept_id")) {
                ps.setString(1, month);
                try (ResultSet rs = ps.executeQuery()) { while (rs.next()) getOrCreate(map, rs.getInt("dept_id"), month).setWasteCo2(rs.getBigDecimal("s")); }
            }
            // Persist
            try (PreparedStatement del = c.prepareStatement("DELETE FROM Carbon_Report WHERE usage_month=?")) {
                del.setString(1, month); del.executeUpdate();
            }
            try (PreparedStatement ins = c.prepareStatement("INSERT INTO Carbon_Report (dept_id, usage_month, energy_co2, water_co2, transport_co2, waste_co2, total_co2) VALUES (?,?,?,?,?,?,?)")) {
                for (CarbonReport r : map.values()) {
                    BigDecimal total = nz(r.getEnergyCo2()).add(nz(r.getWaterCo2())).add(nz(r.getTransportCo2())).add(nz(r.getWasteCo2())).setScale(2, RoundingMode.HALF_UP);
                    r.setTotalCo2(total);
                    if (r.getDeptId() == null) ins.setNull(1, Types.INTEGER); else ins.setInt(1, r.getDeptId());
                    ins.setString(2, month);
                    ins.setBigDecimal(3, r.getEnergyCo2());
                    ins.setBigDecimal(4, r.getWaterCo2());
                    ins.setBigDecimal(5, r.getTransportCo2());
                    ins.setBigDecimal(6, r.getWasteCo2());
                    ins.setBigDecimal(7, total);
                    ins.addBatch();
                }
                ins.executeBatch();
            }
        }
        return new ArrayList<>(map.values());
    }

    private CarbonReport getOrCreate(Map<Integer, CarbonReport> m, int deptId, String month) {
        return m.computeIfAbsent(deptId, k -> { CarbonReport r = new CarbonReport(); r.setDeptId(deptId); r.setUsageMonth(month); return r; });
    }

    public List<CarbonReport> reportsForMonth(String month) throws SQLException {
        List<CarbonReport> list = new ArrayList<>();
        String sql = "SELECT * FROM Carbon_Report WHERE usage_month=? ORDER BY total_co2 DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CarbonReport r = new CarbonReport();
                    r.setReportId(rs.getInt("report_id"));
                    int d = rs.getInt("dept_id"); r.setDeptId(rs.wasNull() ? null : d);
                    r.setUsageMonth(rs.getString("usage_month"));
                    r.setEnergyCo2(rs.getBigDecimal("energy_co2"));
                    r.setWaterCo2(rs.getBigDecimal("water_co2"));
                    r.setTransportCo2(rs.getBigDecimal("transport_co2"));
                    r.setWasteCo2(rs.getBigDecimal("waste_co2"));
                    r.setTotalCo2(rs.getBigDecimal("total_co2"));
                    list.add(r);
                }
            }
        }
        return list;
    }

    public BigDecimal campusTotal(String month) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COALESCE(SUM(total_co2),0) FROM Carbon_Report WHERE usage_month=?")) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO; }
        }
    }

    public BigDecimal totalTreeAbsorption() throws SQLException {
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(SUM(carbon_absorption_kg),0) FROM Tree_Tracker")) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }

    public BigDecimal netCampusFootprint(String month) throws SQLException {
        return campusTotal(month).subtract(totalTreeAbsorption()).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}
