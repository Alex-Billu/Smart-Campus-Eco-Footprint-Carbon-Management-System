package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public boolean insert(Department d) throws SQLException {
        String sql = "INSERT INTO Department (dept_name, dept_code, building, floor_area_sqm, staff_count) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.getDeptName());
            ps.setString(2, d.getDeptCode());
            ps.setString(3, d.getBuilding());
            ps.setBigDecimal(4, d.getFloorAreaSqm());
            ps.setInt(5, d.getStaffCount());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(Department d) throws SQLException {
        String sql = "UPDATE Department SET dept_name=?, dept_code=?, building=?, floor_area_sqm=?, staff_count=? WHERE dept_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.getDeptName());
            ps.setString(2, d.getDeptCode());
            ps.setString(3, d.getBuilding());
            ps.setBigDecimal(4, d.getFloorAreaSqm());
            ps.setInt(5, d.getStaffCount());
            ps.setInt(6, d.getDeptId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Department WHERE dept_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public Department findById(int id) throws SQLException {
        String sql = "SELECT * FROM Department WHERE dept_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<Department> findAll() throws SQLException {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM Department ORDER BY dept_code";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Department> search(String q) throws SQLException {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM Department WHERE dept_name LIKE ? OR dept_code LIKE ? ORDER BY dept_code";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String k = "%" + q + "%";
            ps.setString(1, k);
            ps.setString(2, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    private Department map(ResultSet rs) throws SQLException {
        Department d = new Department();
        d.setDeptId(rs.getInt("dept_id"));
        d.setDeptName(rs.getString("dept_name"));
        d.setDeptCode(rs.getString("dept_code"));
        d.setBuilding(rs.getString("building"));
        d.setFloorAreaSqm(rs.getBigDecimal("floor_area_sqm"));
        d.setStaffCount(rs.getInt("staff_count"));
        return d;
    }
}
