package com.campus.eco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class CarbonFactorsTest {

    @Test
    void calculatesEnergyEmissionsAndRoundsHalfUp() {
        assertEquals(new BigDecimal("82.21"), CarbonFactors.energyCo2(new BigDecimal("100.25")));
    }

    @Test
    void calculatesWaterEmissions() {
        assertEquals(new BigDecimal("0.30"), CarbonFactors.waterCo2(new BigDecimal("1000")));
    }

    @Test
    void selectsDieselFactorWithoutCaseSensitivity() {
        assertEquals(new BigDecimal("26.80"),
                CarbonFactors.transportCo2(new BigDecimal("10"), "DIESEL"));
    }

    @Test
    void defaultsToPetrolFactorForOtherAndMissingVehicleTypes() {
        assertEquals(new BigDecimal("23.10"),
                CarbonFactors.transportCo2(new BigDecimal("10"), "hybrid"));
        assertEquals(new BigDecimal("23.10"),
                CarbonFactors.transportCo2(new BigDecimal("10"), null));
    }

    @Test
    void treatsMissingWasteAmountsAsZero() {
        assertEquals(new BigDecimal("1.55"),
                CarbonFactors.wasteCo2(new BigDecimal("10"), new BigDecimal("3")));
        assertEquals(new BigDecimal("0.00"), CarbonFactors.wasteCo2(null, null));
    }

    @Test
    void calculatesTreeAbsorption() {
        assertEquals(new BigDecimal("43.54"), CarbonFactors.treeAbsorption(2));
    }
}
