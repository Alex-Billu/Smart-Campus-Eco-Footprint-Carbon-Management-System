package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.CarbonBudget;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;
public class CarbonBudgetDAO {

    public boolean upsert(CarbonBudget b) throws SQLException {
        String sql = "INSERT INTO Carbon_Budget (dept_id, usage_month, budget_limit_kg, used_kg, status) VALUES (?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE budget_limit_kg=VALUES(budget_limit_kg), used_kg=VALUES(used_kg), status=VALUES(status)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, b.getDeptId());
            ps.setString(2, b.getUsageMonth());
            ps.setBigDecimal(3, b.getBudgetLimitKg());
            ps.setBigDecimal(4, b.getUsedKg() == null ? BigDecimal.ZERO : b.getUsedKg());
            ps.setString(5, b.getStatus() == null ? "OK" : b.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public List<CarbonBudget> findAll() throws SQLException {
        List<CarbonBudget> list = new ArrayList<>();
        String sql = "SELECT * FROM Carbon_Budget ORDER BY usage_month DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public CarbonBudget find(int deptId, String month) throws SQLException {
        String sql = "SELECT * FROM Carbon_Budget WHERE dept_id=? AND usage_month=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            ps.setString(2, month);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Carbon_Budget WHERE budget_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private CarbonBudget map(ResultSet rs) throws SQLException {
        CarbonBudget b = new CarbonBudget();
        b.setBudgetId(rs.getInt("budget_id"));
        b.setDeptId(rs.getInt("dept_id"));
        b.setUsageMonth(rs.getString("usage_month"));
        b.setBudgetLimitKg(rs.getBigDecimal("budget_limit_kg"));
        b.setUsedKg(rs.getBigDecimal("used_kg"));
        b.setStatus(rs.getString("status"));
        return b;
    }
}
