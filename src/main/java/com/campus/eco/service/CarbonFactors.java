package com.campus.eco.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Carbon emission factors (kg CO2 per unit).
 * Source: IPCC / DEFRA / Indian GHG protocol simplified factors.
 */
public final class CarbonFactors {
    private CarbonFactors() {}

    public static final BigDecimal ELECTRICITY_KG_PER_KWH = new BigDecimal("0.82");
    public static final BigDecimal WATER_KG_PER_LITRE = new BigDecimal("0.0003");
    public static final BigDecimal PETROL_KG_PER_LITRE = new BigDecimal("2.31");
    public static final BigDecimal DIESEL_KG_PER_LITRE = new BigDecimal("2.68");
    public static final BigDecimal WASTE_NONREC_KG_PER_KG = new BigDecimal("0.45");
    public static final BigDecimal WASTE_REC_KG_PER_KG = new BigDecimal("0.02");
    public static final BigDecimal TREE_ABSORPTION_KG_PER_YEAR = new BigDecimal("21.77");

    public static BigDecimal energyCo2(BigDecimal kwh) { return kwh.multiply(ELECTRICITY_KG_PER_KWH).setScale(2, RoundingMode.HALF_UP); }
    public static BigDecimal waterCo2(BigDecimal litres) { return litres.multiply(WATER_KG_PER_LITRE).setScale(2, RoundingMode.HALF_UP); }
    public static BigDecimal transportCo2(BigDecimal fuelLitres, String vehicleType) {
        BigDecimal factor = (vehicleType != null && vehicleType.toLowerCase().contains("diesel")) ? DIESEL_KG_PER_LITRE : PETROL_KG_PER_LITRE;
        return fuelLitres.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
    public static BigDecimal wasteCo2(BigDecimal rec, BigDecimal nonRec) {
        rec = rec == null ? BigDecimal.ZERO : rec;
        nonRec = nonRec == null ? BigDecimal.ZERO : nonRec;
        return rec.multiply(WASTE_REC_KG_PER_KG).add(nonRec.multiply(WASTE_NONREC_KG_PER_KG)).setScale(2, RoundingMode.HALF_UP);
    }
    public static BigDecimal treeAbsorption(int trees) {
        return BigDecimal.valueOf(trees).multiply(TREE_ABSORPTION_KG_PER_YEAR).setScale(2, RoundingMode.HALF_UP);
    }
}
