package com.campus.eco.service;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.AlertLog;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.campus.eco.dao.AlertLogDAO;
public class AlertService {

    private final AlertLogDAO dao = new AlertLogDAO();

    /** Scan latest month data and emit alerts for thresholds exceeded. */
    public List<AlertLog> scanAndPersist(String month) throws SQLException {
        List<AlertLog> created = new ArrayList<>();
        try (Connection c = DBConnection.getConnection()) {
            // Energy: > 1500 kWh per dept per month
            created.addAll(scanMetric(c, month, "Energy_Usage", "electricity_kwh", "co2_emission_kg",
                    new BigDecimal("1500"), "High Electricity Usage", "Electricity usage above 1500 kWh in " + month));
            // Water: > 50000 L
            created.addAll(scanMetric(c, month, "Water_Usage", "water_litres", "co2_emission_kg",
                    new BigDecimal("50000"), "Excess Water Usage", "Water usage above 50000 L in " + month));
            // Waste non-recyclable: > 200 kg
            created.addAll(scanMetric(c, month, "Waste_Record", "non_recyclable_kg", "co2_emission_kg",
                    new BigDecimal("200"), "Waste Overflow", "Non-recyclable waste above 200 kg in " + month));
            // Carbon budget
            created.addAll(scanBudget(c, month));
        }
        for (AlertLog a : created) dao.insert(a);
        return created;
    }

    private List<AlertLog> scanMetric(Connection c, String month, String table, String valueCol, String co2Col,
                                      BigDecimal threshold, String type, String msgTpl) throws SQLException {
        List<AlertLog> list = new ArrayList<>();
        String sql = "SELECT dept_id, " + valueCol + " v FROM " + table + " WHERE usage_month=? AND " + valueCol + " > ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, month);
            ps.setBigDecimal(2, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AlertLog a = new AlertLog();
                    a.setDeptId(rs.getInt("dept_id"));
                    a.setAlertType(type);
                    a.setSeverity("HIGH");
                    a.setMessage(msgTpl + " (value=" + rs.getBigDecimal("v") + ")");
                    a.setUsageMonth(month);
                    list.add(a);
                }
            }
        }
        return list;
    }

    private List<AlertLog> scanBudget(Connection c, String month) throws SQLException {
        List<AlertLog> list = new ArrayList<>();
        String sql = "SELECT b.dept_id, b.budget_limit_kg, COALESCE(r.total_co2,0) used " +
                "FROM Carbon_Budget b LEFT JOIN Carbon_Report r ON b.dept_id=r.dept_id AND b.usage_month=r.usage_month " +
                "WHERE b.usage_month=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BigDecimal limit = rs.getBigDecimal("budget_limit_kg");
                    BigDecimal used = rs.getBigDecimal("used");
                    if (used != null && limit != null && used.compareTo(limit) > 0) {
                        AlertLog a = new AlertLog();
                        a.setDeptId(rs.getInt("dept_id"));
                        a.setAlertType("Carbon Limit Exceeded");
                        a.setSeverity("CRITICAL");
                        a.setMessage("Carbon budget " + limit + " kg exceeded; used " + used + " kg in " + month);
                        a.setUsageMonth(month);
                        list.add(a);
                        // update status
                        try (PreparedStatement us = c.prepareStatement("UPDATE Carbon_Budget SET status='OVER' WHERE dept_id=? AND usage_month=?")) {
                            us.setInt(1, a.getDeptId()); us.setString(2, month); us.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement us = c.prepareStatement("UPDATE Carbon_Budget SET status='OK' WHERE dept_id=? AND usage_month=?")) {
                            us.setInt(1, rs.getInt("dept_id")); us.setString(2, month); us.executeUpdate();
                        }
                    }
                }
            }
        }
        return list;
    }
}
