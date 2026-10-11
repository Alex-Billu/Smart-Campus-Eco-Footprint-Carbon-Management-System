package com.campus.eco.model;

import java.math.BigDecimal;

public class CarbonBudget {
    private int budgetId;
    private int deptId;
    private String usageMonth;
    private BigDecimal budgetLimitKg;
    private BigDecimal usedKg;
    private String status;

    public CarbonBudget() {}

    public int getBudgetId() { return budgetId; }
    public void setBudgetId(int budgetId) { this.budgetId = budgetId; }
    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public BigDecimal getBudgetLimitKg() { return budgetLimitKg; }
    public void setBudgetLimitKg(BigDecimal budgetLimitKg) { this.budgetLimitKg = budgetLimitKg; }
    public BigDecimal getUsedKg() { return usedKg; }
    public void setUsedKg(BigDecimal usedKg) { this.usedKg = usedKg; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
