package com.campus.eco.model;

import java.math.BigDecimal;

public class WasteRecord {
    private int wasteId;
    private int deptId;
    private String usageMonth;
    private BigDecimal recyclableKg;
    private BigDecimal nonRecyclableKg;
    private BigDecimal co2EmissionKg;

    public WasteRecord() {}

    public int getWasteId() { return wasteId; }
    public void setWasteId(int wasteId) { this.wasteId = wasteId; }
    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public BigDecimal getRecyclableKg() { return recyclableKg; }
    public void setRecyclableKg(BigDecimal recyclableKg) { this.recyclableKg = recyclableKg; }
    public BigDecimal getNonRecyclableKg() { return nonRecyclableKg; }
    public void setNonRecyclableKg(BigDecimal nonRecyclableKg) { this.nonRecyclableKg = nonRecyclableKg; }
    public BigDecimal getCo2EmissionKg() { return co2EmissionKg; }
    public void setCo2EmissionKg(BigDecimal co2EmissionKg) { this.co2EmissionKg = co2EmissionKg; }
}
