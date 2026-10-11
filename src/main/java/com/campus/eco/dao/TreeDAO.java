package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.TreeTracker;
import com.campus.eco.service.CarbonFactors;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreeDAO {

    public boolean insert(TreeTracker t) throws SQLException {
        String sql = "INSERT INTO Tree_Tracker (dept_id, tree_count, species, planted_date, carbon_absorption_kg, location) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (t.getDeptId() == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, t.getDeptId());
            ps.setInt(2, t.getTreeCount());
            ps.setString(3, t.getSpecies());
            ps.setDate(4, Date.valueOf(t.getPlantedDate()));
            ps.setBigDecimal(5, CarbonFactors.treeAbsorption(t.getTreeCount()));
            ps.setString(6, t.getLocation());
            return ps.executeUpdate() > 0;
        }
    }

    public List<TreeTracker> findAll() throws SQLException {
        List<TreeTracker> list = new ArrayList<>();
        String sql = "SELECT * FROM Tree_Tracker ORDER BY planted_date DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Tree_Tracker WHERE tree_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public int totalTrees() throws SQLException {
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(SUM(tree_count),0) FROM Tree_Tracker")) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private TreeTracker map(ResultSet rs) throws SQLException {
        TreeTracker t = new TreeTracker();
        t.setTreeId(rs.getInt("tree_id"));
        int d = rs.getInt("dept_id");
        t.setDeptId(rs.wasNull() ? null : d);
        t.setTreeCount(rs.getInt("tree_count"));
        t.setSpecies(rs.getString("species"));
        Date dt = rs.getDate("planted_date");
        if (dt != null) t.setPlantedDate(dt.toLocalDate());
        t.setCarbonAbsorptionKg(rs.getBigDecimal("carbon_absorption_kg"));
        t.setLocation(rs.getString("location"));
        return t;
    }
}
