package com.campus.eco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class SimulatorServiceTest {

    private final SimulatorService service = new SimulatorService();

    @Test
    void combinesEnergyTreeAndTransportSavings() {
        SimulatorService.SimResult result = service.simulate(10, 2, 100);

        assertEquals(new BigDecimal("52.80"), result.energySavedKwh);
        assertEquals(new BigDecimal("109.94"), result.totalCarbonSaved);
        assertEquals(new BigDecimal("1122.40"), result.totalMoneySaved);
        assertEquals(new BigDecimal("43.30"), result.carbonEnergy);
        assertEquals(new BigDecimal("43.54"), result.carbonTrees);
        assertEquals(new BigDecimal("23.10"), result.carbonTransport);
        assertEquals(new BigDecimal("422.40"), result.moneyEnergy);
        assertEquals(new BigDecimal("700.00"), result.moneyTransport);
    }

    @Test
    void returnsZeroSavingsWhenNoMeasuresAreProvided() {
        SimulatorService.SimResult result = service.simulate(0, 0, 0);

        assertEquals(new BigDecimal("0.00"), result.energySavedKwh);
        assertEquals(new BigDecimal("0.00"), result.totalCarbonSaved);
        assertEquals(new BigDecimal("0.00"), result.totalMoneySaved);
        assertEquals(new BigDecimal("0.00"), result.carbonEnergy);
        assertEquals(new BigDecimal("0.00"), result.carbonTrees);
        assertEquals(new BigDecimal("0.00"), result.carbonTransport);
        assertEquals(new BigDecimal("0.00"), result.moneyEnergy);
        assertEquals(new BigDecimal("0.00"), result.moneyTransport);
    }
}
