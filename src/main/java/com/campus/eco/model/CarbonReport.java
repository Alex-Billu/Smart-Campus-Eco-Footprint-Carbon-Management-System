package com.campus.eco.model;

import java.math.BigDecimal;

public class CarbonReport {
    private int reportId;
    private Integer deptId;
    private String usageMonth;
    private BigDecimal energyCo2;
    private BigDecimal waterCo2;
    private BigDecimal transportCo2;
    private BigDecimal wasteCo2;
    private BigDecimal totalCo2;

    public CarbonReport() {}

    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public BigDecimal getEnergyCo2() { return energyCo2; }
    public void setEnergyCo2(BigDecimal energyCo2) { this.energyCo2 = energyCo2; }
    public BigDecimal getWaterCo2() { return waterCo2; }
    public void setWaterCo2(BigDecimal waterCo2) { this.waterCo2 = waterCo2; }
    public BigDecimal getTransportCo2() { return transportCo2; }
    public void setTransportCo2(BigDecimal transportCo2) { this.transportCo2 = transportCo2; }
    public BigDecimal getWasteCo2() { return wasteCo2; }
    public void setWasteCo2(BigDecimal wasteCo2) { this.wasteCo2 = wasteCo2; }
    public BigDecimal getTotalCo2() { return totalCo2; }
    public void setTotalCo2(BigDecimal totalCo2) { this.totalCo2 = totalCo2; }
}
