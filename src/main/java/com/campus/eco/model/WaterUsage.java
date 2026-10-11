package com.campus.eco.model;

import java.math.BigDecimal;

public class WaterUsage {
    private int waterId;
    private int deptId;
    private String usageMonth;
    private BigDecimal waterLitres;
    private BigDecimal costAmount;
    private BigDecimal co2EmissionKg;

    public WaterUsage() {}

    public int getWaterId() { return waterId; }
    public void setWaterId(int waterId) { this.waterId = waterId; }
    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public BigDecimal getWaterLitres() { return waterLitres; }
    public void setWaterLitres(BigDecimal waterLitres) { this.waterLitres = waterLitres; }
    public BigDecimal getCostAmount() { return costAmount; }
    public void setCostAmount(BigDecimal costAmount) { this.costAmount = costAmount; }
    public BigDecimal getCo2EmissionKg() { return co2EmissionKg; }
    public void setCo2EmissionKg(BigDecimal co2EmissionKg) { this.co2EmissionKg = co2EmissionKg; }
}
