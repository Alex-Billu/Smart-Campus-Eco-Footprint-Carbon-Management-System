package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.SustainabilityScore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SustainabilityScoreDAO {

    public boolean upsert(SustainabilityScore s) throws SQLException {
        String sql = "INSERT INTO Sustainability_Score (dept_id, usage_month, energy_score, water_score, waste_score, transport_score, overall_score, rating) " +
                "VALUES (?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE energy_score=VALUES(energy_score), water_score=VALUES(water_score), " +
                "waste_score=VALUES(waste_score), transport_score=VALUES(transport_score), overall_score=VALUES(overall_score), rating=VALUES(rating)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getDeptId());
            ps.setString(2, s.getUsageMonth());
            ps.setBigDecimal(3, s.getEnergyScore());
            ps.setBigDecimal(4, s.getWaterScore());
            ps.setBigDecimal(5, s.getWasteScore());
            ps.setBigDecimal(6, s.getTransportScore());
            ps.setBigDecimal(7, s.getOverallScore());
            ps.setString(8, s.getRating());
            return ps.executeUpdate() > 0;
        }
    }

    public List<SustainabilityScore> findByMonth(String month) throws SQLException {
        List<SustainabilityScore> list = new ArrayList<>();
        String sql = "SELECT * FROM Sustainability_Score WHERE usage_month=? ORDER BY overall_score DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<SustainabilityScore> findAll() throws SQLException {
        List<SustainabilityScore> list = new ArrayList<>();
        String sql = "SELECT * FROM Sustainability_Score ORDER BY usage_month DESC, overall_score DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private SustainabilityScore map(ResultSet rs) throws SQLException {
        SustainabilityScore s = new SustainabilityScore();
        s.setScoreId(rs.getInt("score_id"));
        s.setDeptId(rs.getInt("dept_id"));
        s.setUsageMonth(rs.getString("usage_month"));
        s.setEnergyScore(rs.getBigDecimal("energy_score"));
        s.setWaterScore(rs.getBigDecimal("water_score"));
        s.setWasteScore(rs.getBigDecimal("waste_score"));
        s.setTransportScore(rs.getBigDecimal("transport_score"));
        s.setOverallScore(rs.getBigDecimal("overall_score"));
        s.setRating(rs.getString("rating"));
        return s;
    }
}
