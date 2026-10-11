package com.campus.eco.model;

import java.math.BigDecimal;

public class CostAnalysis {
    private int costId;
    private Integer deptId;
    private String usageMonth;
    private BigDecimal energyCost;
    private BigDecimal waterCost;
    private BigDecimal transportCost;
    private BigDecimal wasteCost;
    private BigDecimal totalCost;
    private BigDecimal totalCo2;
    private BigDecimal costPerCo2;

    public CostAnalysis() {}

    public int getCostId() { return costId; }
    public void setCostId(int costId) { this.costId = costId; }
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public BigDecimal getEnergyCost() { return energyCost; }
    public void setEnergyCost(BigDecimal energyCost) { this.energyCost = energyCost; }
    public BigDecimal getWaterCost() { return waterCost; }
    public void setWaterCost(BigDecimal waterCost) { this.waterCost = waterCost; }
    public BigDecimal getTransportCost() { return transportCost; }
    public void setTransportCost(BigDecimal transportCost) { this.transportCost = transportCost; }
    public BigDecimal getWasteCost() { return wasteCost; }
    public void setWasteCost(BigDecimal wasteCost) { this.wasteCost = wasteCost; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public BigDecimal getTotalCo2() { return totalCo2; }
    public void setTotalCo2(BigDecimal totalCo2) { this.totalCo2 = totalCo2; }
    public BigDecimal getCostPerCo2() { return costPerCo2; }
    public void setCostPerCo2(BigDecimal costPerCo2) { this.costPerCo2 = costPerCo2; }
}
