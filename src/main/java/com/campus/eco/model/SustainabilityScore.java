package com.campus.eco.model;

import java.math.BigDecimal;

public class SustainabilityScore {
    private int scoreId;
    private int deptId;
    private String usageMonth;
    private BigDecimal energyScore;
    private BigDecimal waterScore;
    private BigDecimal wasteScore;
    private BigDecimal transportScore;
    private BigDecimal overallScore;
    private String rating;

    public SustainabilityScore() {}

    public int getScoreId() { return scoreId; }
    public void setScoreId(int scoreId) { this.scoreId = scoreId; }
    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public BigDecimal getEnergyScore() { return energyScore; }
    public void setEnergyScore(BigDecimal energyScore) { this.energyScore = energyScore; }
    public BigDecimal getWaterScore() { return waterScore; }
    public void setWaterScore(BigDecimal waterScore) { this.waterScore = waterScore; }
    public BigDecimal getWasteScore() { return wasteScore; }
    public void setWasteScore(BigDecimal wasteScore) { this.wasteScore = wasteScore; }
    public BigDecimal getTransportScore() { return transportScore; }
    public void setTransportScore(BigDecimal transportScore) { this.transportScore = transportScore; }
    public BigDecimal getOverallScore() { return overallScore; }
    public void setOverallScore(BigDecimal overallScore) { this.overallScore = overallScore; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
}
