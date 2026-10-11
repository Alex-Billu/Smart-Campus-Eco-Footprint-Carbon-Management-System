package com.campus.eco.service;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.SustainabilityScore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Computes sustainability scores (0-100) per department per month.
 * Lower carbon -> higher score. Benchmarked against campus max.
 */
import com.campus.eco.dao.SustainabilityScoreDAO;
public class SustainabilityScoreService {

    private final SustainabilityScoreDAO dao = new SustainabilityScoreDAO();

    public List<SustainabilityScore> computeAndPersist(String month) throws SQLException {
        List<SustainabilityScore> list = new ArrayList<>();
        String sql = "SELECT dept_id, energy_co2, water_co2, transport_co2, waste_co2, total_co2 FROM Carbon_Report WHERE usage_month=?";
        BigDecimal maxTotal = BigDecimal.ONE;
        BigDecimal maxE = BigDecimal.ONE, maxW = BigDecimal.ONE, maxT = BigDecimal.ONE, maxWa = BigDecimal.ONE;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BigDecimal e = nz(rs.getBigDecimal("energy_co2"));
                    BigDecimal w = nz(rs.getBigDecimal("water_co2"));
                    BigDecimal t = nz(rs.getBigDecimal("transport_co2"));
                    BigDecimal wa = nz(rs.getBigDecimal("waste_co2"));
                    BigDecimal tot = nz(rs.getBigDecimal("total_co2"));
                    SustainabilityScore s = new SustainabilityScore();
                    s.setDeptId(rs.getInt("dept_id"));
                    s.setUsageMonth(month);
                    s.setEnergyScore(e);
                    s.setWaterScore(w);
                    s.setTransportScore(t);
                    s.setWasteScore(wa);
                    s.setOverallScore(tot);
                    list.add(s);
                    if (e.compareTo(maxE) > 0) maxE = e;
                    if (w.compareTo(maxW) > 0) maxW = w;
                    if (t.compareTo(maxT) > 0) maxT = t;
                    if (wa.compareTo(maxWa) > 0) maxWa = wa;
                    if (tot.compareTo(maxTotal) > 0) maxTotal = tot;
                }
            }
            for (SustainabilityScore s : list) {
                s.setEnergyScore(score(s.getEnergyScore(), maxE));
                s.setWaterScore(score(s.getWaterScore(), maxW));
                s.setTransportScore(score(s.getTransportScore(), maxT));
                s.setWasteScore(score(s.getWasteScore(), maxWa));
                s.setOverallScore(score(s.getOverallScore(), maxTotal));
                s.setRating(rating(s.getOverallScore()));
                dao.upsert(s);
            }
        }
        return list;
    }

    private BigDecimal score(BigDecimal val, BigDecimal max) {
        if (val == null || max == null || max.signum() == 0) return new BigDecimal("100");
        return BigDecimal.ONE.subtract(val.divide(max, 4, RoundingMode.HALF_UP)).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
    }
    private String rating(BigDecimal overall) {
        double v = overall == null ? 0 : overall.doubleValue();
        if (v >= 80) return "A+";
        if (v >= 65) return "A";
        if (v >= 50) return "B";
        if (v >= 35) return "C";
        return "D";
    }
    private BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}
