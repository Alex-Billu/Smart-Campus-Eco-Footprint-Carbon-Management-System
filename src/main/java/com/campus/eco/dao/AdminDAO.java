package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.Admin;
import com.campus.eco.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public Admin login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Admin WHERE username = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password_hash");
                    if (PasswordUtil.verify(password, hash)) {
                        Admin a = new Admin();
                        a.setAdminId(rs.getInt("admin_id"));
                        a.setUsername(rs.getString("username"));
                        a.setPasswordHash(hash);
                        a.setFullName(rs.getString("full_name"));
                        a.setEmail(rs.getString("email"));
                        return a;
                    }
                }
            }
        }
        return null;
    }

    public boolean changePassword(String username, String newPlain) throws SQLException {
        String sql = "UPDATE Admin SET password_hash = ? WHERE username = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, PasswordUtil.hash(newPlain));
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM Admin WHERE username = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public List<Admin> findAll() throws SQLException {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT * FROM Admin ORDER BY admin_id";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Admin a = new Admin();
                a.setAdminId(rs.getInt("admin_id"));
                a.setUsername(rs.getString("username"));
                a.setFullName(rs.getString("full_name"));
                a.setEmail(rs.getString("email"));
                list.add(a);
            }
        }
        return list;
    }
}
