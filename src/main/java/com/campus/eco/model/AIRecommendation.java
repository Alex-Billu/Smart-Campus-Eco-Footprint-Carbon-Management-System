package com.campus.eco.model;

public class AIRecommendation {
    private int recId;
    private Integer deptId;
    private String usageMonth;
    private String category;
    private String recommendation;
    private String priority;

    public AIRecommendation() {}

    public int getRecId() { return recId; }
    public void setRecId(int recId) { this.recId = recId; }
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public String getUsageMonth() { return usageMonth; }
    public void setUsageMonth(String usageMonth) { this.usageMonth = usageMonth; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
