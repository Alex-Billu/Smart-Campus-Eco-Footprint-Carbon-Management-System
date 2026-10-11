package com.campus.eco.service;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.CostAnalysis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cost vs Carbon analyzer. Aggregates cost data per dept/month and computes cost-per-CO2.
 */
import com.campus.eco.dao.CostAnalysisDAO;
public class CostAnalysisService {

    private final CostAnalysisDAO dao = new CostAnalysisDAO();

    public List<CostAnalysis> generateForMonth(String month) throws SQLException {
        List<CostAnalysis> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT DISTINCT dept_id FROM Carbon_Report WHERE usage_month=?")) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int deptId = rs.getInt("dept_id");
                    BigDecimal ec = sum(c, "SELECT COALESCE(SUM(cost_amount),0) FROM Energy_Usage WHERE dept_id=? AND usage_month=?", deptId, month);
                    BigDecimal wc = sum(c, "SELECT COALESCE(SUM(cost_amount),0) FROM Water_Usage WHERE dept_id=? AND usage_month=?", deptId, month);
                    BigDecimal tc = sum(c, "SELECT COALESCE(SUM(distance_km)*5,0) FROM Transport_Details WHERE dept_id=? AND usage_month=?", deptId, month);
                    BigDecimal wac = sum(c, "SELECT COALESCE(SUM(non_recyclable_kg)*2,0) FROM Waste_Record WHERE dept_id=? AND usage_month=?", deptId, month);
                    BigDecimal totalCost = ec.add(wc).add(tc).add(wac).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal totalCo2 = sum(c, "SELECT COALESCE(total_co2,0) FROM Carbon_Report WHERE dept_id=? AND usage_month=?", deptId, month);
                    BigDecimal perCo2 = totalCo2.signum() == 0 ? BigDecimal.ZERO : totalCost.divide(totalCo2, 4, RoundingMode.HALF_UP);
                    CostAnalysis ca = new CostAnalysis();
                    ca.setDeptId(deptId); ca.setUsageMonth(month);
                    ca.setEnergyCost(ec); ca.setWaterCost(wc); ca.setTransportCost(tc); ca.setWasteCost(wac);
                    ca.setTotalCost(totalCost); ca.setTotalCo2(totalCo2); ca.setCostPerCo2(perCo2);
                    list.add(ca);
                    dao.insert(ca);
                }
            }
        }
        return list;
    }

    public List<CostAnalysis> all() throws SQLException { return dao.findAll(); }

    private BigDecimal sum(Connection c, String sql, int deptId, String month) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, deptId); ps.setString(2, month);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO; }
        }
    }
}
