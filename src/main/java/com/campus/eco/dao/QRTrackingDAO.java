package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.QRTracking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QRTrackingDAO {

    public boolean insert(QRTracking q) throws SQLException {
        String sql = "INSERT INTO QR_Tracking (dept_id, resource_type, encoded_data) VALUES (?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (q.getDeptId() == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, q.getDeptId());
            ps.setString(2, q.getResourceType());
            ps.setString(3, q.getEncodedData());
            return ps.executeUpdate() > 0;
        }
    }

    public List<QRTracking> findAll() throws SQLException {
        List<QRTracking> list = new ArrayList<>();
        String sql = "SELECT * FROM QR_Tracking ORDER BY generated_at DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    private QRTracking map(ResultSet rs) throws SQLException {
        QRTracking q = new QRTracking();
        q.setQrId(rs.getInt("qr_id"));
        int d = rs.getInt("dept_id");
        q.setDeptId(rs.wasNull() ? null : d);
        q.setResourceType(rs.getString("resource_type"));
        q.setEncodedData(rs.getString("encoded_data"));
        return q;
    }
}
