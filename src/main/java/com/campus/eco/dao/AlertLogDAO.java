package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.AlertLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertLogDAO {

    public boolean insert(AlertLog a) throws SQLException {
        String sql = "INSERT INTO Alert_Log (dept_id, alert_type, severity, message, usage_month) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (a.getDeptId() == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, a.getDeptId());
            ps.setString(2, a.getAlertType());
            ps.setString(3, a.getSeverity());
            ps.setString(4, a.getMessage());
            ps.setString(5, a.getUsageMonth());
            return ps.executeUpdate() > 0;
        }
    }

    public List<AlertLog> findAll() throws SQLException {
        List<AlertLog> list = new ArrayList<>();
        String sql = "SELECT * FROM Alert_Log ORDER BY created_at DESC LIMIT 200";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private AlertLog map(ResultSet rs) throws SQLException {
        AlertLog a = new AlertLog();
        a.setAlertId(rs.getInt("alert_id"));
        int d = rs.getInt("dept_id");
        a.setDeptId(rs.wasNull() ? null : d);
        a.setAlertType(rs.getString("alert_type"));
        a.setSeverity(rs.getString("severity"));
        a.setMessage(rs.getString("message"));
        a.setUsageMonth(rs.getString("usage_month"));
        return a;
    }
}
