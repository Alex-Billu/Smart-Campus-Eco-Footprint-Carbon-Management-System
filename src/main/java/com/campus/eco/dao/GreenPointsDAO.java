package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.GreenPoints;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GreenPointsDAO {

    public boolean insert(GreenPoints g) throws SQLException {
        String sql = "INSERT INTO Green_Points (dept_id, usage_month, points, reason) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, g.getDeptId());
            ps.setString(2, g.getUsageMonth());
            ps.setInt(3, g.getPoints());
            ps.setString(4, g.getReason());
            return ps.executeUpdate() > 0;
        }
    }

    public List<GreenPoints> findAll() throws SQLException {
        List<GreenPoints> list = new ArrayList<>();
        String sql = "SELECT * FROM Green_Points ORDER BY awarded_at DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public int totalForDept(int deptId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(points),0) FROM Green_Points WHERE dept_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    public List<int[]> leaderboard() throws SQLException {
        List<int[]> list = new ArrayList<>();
        String sql = "SELECT dept_id, SUM(points) AS total FROM Green_Points GROUP BY dept_id ORDER BY total DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new int[]{rs.getInt("dept_id"), rs.getInt("total")});
        }
        return list;
    }

    private GreenPoints map(ResultSet rs) throws SQLException {
        GreenPoints g = new GreenPoints();
        g.setPointId(rs.getInt("point_id"));
        g.setDeptId(rs.getInt("dept_id"));
        g.setUsageMonth(rs.getString("usage_month"));
        g.setPoints(rs.getInt("points"));
        g.setReason(rs.getString("reason"));
        return g;
    }
}
