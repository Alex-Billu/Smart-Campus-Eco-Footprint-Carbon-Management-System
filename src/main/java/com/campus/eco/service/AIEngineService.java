package com.campus.eco.service;

import com.campus.eco.config.DBConnection;
import com.campus.eco.model.AIRecommendation;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Rule-based AI recommendation engine + chat assistant.
 */
import com.campus.eco.dao.AIRecommendationDAO;
public class AIEngineService {

    private final AIRecommendationDAO dao = new AIRecommendationDAO();

    public List<AIRecommendation> generateForDept(int deptId, String month) throws SQLException {
        List<AIRecommendation> recs = new ArrayList<>();
        try (Connection c = DBConnection.getConnection()) {
            BigDecimal elec = sum(c, "SELECT COALESCE(SUM(electricity_kwh),0) FROM Energy_Usage WHERE dept_id=? AND usage_month=?", deptId, month);
            BigDecimal water = sum(c, "SELECT COALESCE(SUM(water_litres),0) FROM Water_Usage WHERE dept_id=? AND usage_month=?", deptId, month);
            BigDecimal fuel = sum(c, "SELECT COALESCE(SUM(fuel_litres),0) FROM Transport_Details WHERE dept_id=? AND usage_month=?", deptId, month);
            BigDecimal wasteNon = sum(c, "SELECT COALESCE(SUM(non_recyclable_kg),0) FROM Waste_Record WHERE dept_id=? AND usage_month=?", deptId, month);

            if (elec.compareTo(new BigDecimal("1500")) > 0)
                recs.add(rec(deptId, month, "Energy", "Install LED lighting and motion sensors to cut electricity use by 25-40%.", "HIGH"));
            else
                recs.add(rec(deptId, month, "Energy", "Maintain current usage; consider solar PV for further savings.", "LOW"));

            if (water.compareTo(new BigDecimal("50000")) > 0)
                recs.add(rec(deptId, month, "Water", "Deploy smart water meters and rainwater harvesting; fix leaks promptly.", "HIGH"));
            else
                recs.add(rec(deptId, month, "Water", "Water usage is within limits; continue low-flow fixtures.", "LOW"));

            if (fuel.compareTo(new BigDecimal("200")) > 0)
                recs.add(rec(deptId, month, "Transport", "Promote carpooling, EV shuttles, and virtual meetings to reduce fuel use.", "MEDIUM"));

            if (wasteNon.compareTo(new BigDecimal("200")) > 0)
                recs.add(rec(deptId, month, "Waste", "Segregate waste at source; partner with recyclers for paper/plastic/e-waste.", "HIGH"));
            else
                recs.add(rec(deptId, month, "Waste", "Good waste segregation; aim for zero-landfill certification.", "LOW"));
        }
        for (AIRecommendation r : recs) dao.insert(r);
        return recs;
    }

    /** Chat-style answer to a natural-language question. */
    public String answer(String question) throws SQLException {
        if (question == null) return "Please ask a question.";
        String q = question.toLowerCase().trim();
        try (Connection c = DBConnection.getConnection()) {
            if (q.contains("tree") && (q.contains("offset") || q.contains("needed") || q.contains("plant"))) {
                BigDecimal total = new CarbonCalculatorService().campusTotal(latestMonth(c));
                int trees = total.divide(CarbonFactors.TREE_ABSORPTION_KG_PER_YEAR, 0, java.math.RoundingMode.UP).intValue();
                return "To offset the current campus carbon footprint of " + total + " kg CO2, approximately " + trees + " mature trees are needed (each absorbs ~21.77 kg CO2/year).";
            }
            if (q.contains("reduce") && (q.contains("emission") || q.contains("carbon"))) {
                return "Top carbon reduction strategies:\n1. Switch to LED lighting & motion sensors\n2. Install solar panels on rooftops\n3. Promote carpooling & EV shuttles\n4. Rainwater harvesting & low-flow fixtures\n5. Waste segregation & composting\n6. Plant native trees on campus";
            }
            if (q.contains("electricity") && q.contains("increase")) {
                BigDecimal prev = sum(c, "SELECT COALESCE(SUM(electricity_kwh),0) FROM Energy_Usage WHERE usage_month=?", null, prevMonth());
                BigDecimal cur = sum(c, "SELECT COALESCE(SUM(electricity_kwh),0) FROM Energy_Usage WHERE usage_month=?", null, latestMonth(c));
                BigDecimal diff = cur.subtract(prev);
                return "Electricity usage " + (diff.signum() >= 0 ? "increased" : "decreased") + " by " + diff.abs() + " kWh vs last month. Common causes: HVAC load, lab equipment, lighting. Recommend LED + smart AC scheduling.";
            }
            if (q.contains("waste") && q.contains("reduce")) {
                return "Best ways to reduce waste:\n- Segregate at source (wet/dry/e-waste)\n- Go paperless in admin\n- Compost organic waste\n- Recycle plastics & metals via certified vendors\n- Refill stations to cut PET bottle use";
            }
            if (q.contains("water")) {
                return "To reduce water usage: fix leaks, install low-flow taps, rainwater harvesting, sensor urinals, and reuse treated water for gardens.";
            }
            if (q.contains("cse") || q.contains("department")) {
                return "For CSE department: optimize lab AC schedules, enable power-saving on PCs, virtualize servers, promote carpooling, and plant trees around the block.";
            }
            return "I can help with: carbon reduction, electricity analysis, water saving, waste management, tree offset estimates, and department-specific tips. Try: 'How can CSE reduce emissions?' or 'How many trees to offset emissions?'";
        }
    }

    private String latestMonth(Connection c) throws SQLException {
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT usage_month FROM Energy_Usage ORDER BY usage_month DESC LIMIT 1")) {
            return rs.next() ? rs.getString(1) : "";
        }
    }
    private String prevMonth() { return ""; }
    private BigDecimal sum(Connection c, String sql, Integer deptId, String month) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            if (deptId != null) ps.setInt(i++, deptId);
            ps.setString(i, month);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO; }
        }
    }
    private AIRecommendation rec(int deptId, String month, String cat, String text, String priority) {
        AIRecommendation r = new AIRecommendation();
        r.setDeptId(deptId); r.setUsageMonth(month); r.setCategory(cat); r.setRecommendation(text); r.setPriority(priority);
        return r;
    }
}
