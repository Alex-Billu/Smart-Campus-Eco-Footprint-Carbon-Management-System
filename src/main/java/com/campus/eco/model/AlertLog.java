package com.campus.eco.model;

public class AlertLog {
    private int alertId;
    private Integer deptId;
    private String alertType;
    private String severity;
    private String message;
    private String usageMonth;

    public AlertLog() {}

    public int getAlertId() { return alertId; }
    public void setAlertId(int alertId) { this.alertId = alertId; }
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
}
