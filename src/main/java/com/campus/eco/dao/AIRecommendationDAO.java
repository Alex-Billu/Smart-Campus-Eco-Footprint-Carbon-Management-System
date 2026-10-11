package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.AIRecommendation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AIRecommendationDAO {

    public boolean insert(AIRecommendation r) throws SQLException {
        String sql = "INSERT INTO AI_Recommendations (dept_id, usage_month, category, recommendation, priority) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (r.getDeptId() == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, r.getDeptId());
            ps.setString(2, r.getUsageMonth());
            ps.setString(3, r.getCategory());
            ps.setString(4, r.getRecommendation());
            ps.setString(5, r.getPriority());
            return ps.executeUpdate() > 0;
        }
    }

    public List<AIRecommendation> findAll() throws SQLException {
        List<AIRecommendation> list = new ArrayList<>();
        String sql = "SELECT * FROM AI_Recommendations ORDER BY created_at DESC LIMIT 100";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<AIRecommendation> findByDept(int deptId) throws SQLException {
        List<AIRecommendation> list = new ArrayList<>();
        String sql = "SELECT * FROM AI_Recommendations WHERE dept_id=? ORDER BY created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    private AIRecommendation map(ResultSet rs) throws SQLException {
        AIRecommendation r = new AIRecommendation();
        r.setRecId(rs.getInt("rec_id"));
        int d = rs.getInt("dept_id");
        r.setDeptId(rs.wasNull() ? null : d);
        r.setUsageMonth(rs.getString("usage_month"));
        r.setCategory(rs.getString("category"));
        r.setRecommendation(rs.getString("recommendation"));
        r.setPriority(rs.getString("priority"));
        return r;
    }
}
