package com.campus.eco.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TreeTracker {
    private int treeId;
    private Integer deptId;
    private int treeCount;
    private String species;
    private LocalDate plantedDate;
    private BigDecimal carbonAbsorptionKg;
    private String location;

    public TreeTracker() {}

    public int getTreeId() { return treeId; }
    public void setTreeId(int treeId) { this.treeId = treeId; }
    public Integer getDeptId() { return deptId; }
    public void setDeptId(Integer deptId) { this.deptId = deptId; }
    public int getTreeCount() { return treeCount; }
    public void setTreeCount(int treeCount) { this.treeCount = treeCount; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public LocalDate getPlantedDate() { return plantedDate; }
    public void setPlantedDate(LocalDate plantedDate) { this.plantedDate = plantedDate; }
    public BigDecimal getCarbonAbsorptionKg() { return carbonAbsorptionKg; }
    public void setCarbonAbsorptionKg(BigDecimal carbonAbsorptionKg) { this.carbonAbsorptionKg = carbonAbsorptionKg; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
