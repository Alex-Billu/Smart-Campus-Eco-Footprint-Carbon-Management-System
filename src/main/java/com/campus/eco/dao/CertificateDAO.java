package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.CertificateRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CertificateDAO {

    public boolean insert(CertificateRecord c) throws SQLException {
        String sql = "INSERT INTO Certificate_Record (dept_id, cert_type, cert_month, title, description) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (c.getDeptId() == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, c.getDeptId());
            ps.setString(2, c.getCertType());
            ps.setString(3, c.getCertMonth());
            ps.setString(4, c.getTitle());
            ps.setString(5, c.getDescription());
            return ps.executeUpdate() > 0;
        }
    }

    public List<CertificateRecord> findAll() throws SQLException {
        List<CertificateRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM Certificate_Record ORDER BY issued_at DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private CertificateRecord map(ResultSet rs) throws SQLException {
        CertificateRecord c = new CertificateRecord();
        c.setCertId(rs.getInt("cert_id"));
        int d = rs.getInt("dept_id");
        c.setDeptId(rs.wasNull() ? null : d);
        c.setCertType(rs.getString("cert_type"));
        c.setCertMonth(rs.getString("cert_month"));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        return c;
    }
}
