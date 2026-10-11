package com.campus.eco.dao;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.TransportDetails;
import com.campus.eco.service.CarbonFactors;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransportDAO {

    public boolean insert(TransportDetails t) throws SQLException {
        String sql = "INSERT INTO Transport_Details (dept_id, usage_month, vehicle_type, distance_km, fuel_litres, co2_emission_kg) VALUES (?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getDeptId());
            ps.setString(2, t.getUsageMonth());
            ps.setString(3, t.getVehicleType());
            ps.setBigDecimal(4, t.getDistanceKm());
            ps.setBigDecimal(5, t.getFuelLitres());
            ps.setBigDecimal(6, CarbonFactors.transportCo2(t.getFuelLitres(), t.getVehicleType()));
            return ps.executeUpdate() > 0;
        }
    }

    public List<TransportDetails> findAll() throws SQLException {
        List<TransportDetails> list = new ArrayList<>();
        String sql = "SELECT * FROM Transport_Details ORDER BY usage_month DESC";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<TransportDetails> findByDept(int deptId) throws SQLException {
        List<TransportDetails> list = new ArrayList<>();
        String sql = "SELECT * FROM Transport_Details WHERE dept_id=? ORDER BY usage_month DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Transport_Details WHERE transport_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private TransportDetails map(ResultSet rs) throws SQLException {
        TransportDetails t = new TransportDetails();
        t.setTransportId(rs.getInt("transport_id"));
        t.setDeptId(rs.getInt("dept_id"));
        t.setUsageMonth(rs.getString("usage_month"));
        t.setVehicleType(rs.getString("vehicle_type"));
        t.setDistanceKm(rs.getBigDecimal("distance_km"));
        t.setFuelLitres(rs.getBigDecimal("fuel_litres"));
        t.setCo2EmissionKg(rs.getBigDecimal("co2_emission_kg"));
        return t;
    }
}
