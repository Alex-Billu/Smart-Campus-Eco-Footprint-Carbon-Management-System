package com.campus.eco.model;

import java.math.BigDecimal;

public class EnergyUsage {
    private int energyId;
    private int deptId;
    private String usageMonth;
    private BigDecimal electricityKwh;
    private BigDecimal costAmount;
    private BigDecimal co2EmissionKg;

    public EnergyUsage() {}

    public int getEnergyId() { return energyId; }
    public void setEnergyId(int energyId) { this.energyId = energyId; }
    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public BigDecimal getElectricityKwh() { return electricityKwh; }
    public void setElectricityKwh(BigDecimal electricityKwh) { this.electricityKwh = electricityKwh; }
    public BigDecimal getCostAmount() { return costAmount; }
    public void setCostAmount(BigDecimal costAmount) { this.costAmount = costAmount; }
    public BigDecimal getCo2EmissionKg() { return co2EmissionKg; }
    public void setCo2EmissionKg(BigDecimal co2EmissionKg) { this.co2EmissionKg = co2EmissionKg; }
}
