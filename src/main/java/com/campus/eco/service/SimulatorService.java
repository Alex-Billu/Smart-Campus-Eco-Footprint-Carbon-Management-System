package com.campus.eco.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Carbon Savings Simulator - what-if scenarios.
 */
public class SimulatorService {

    public SimResult simulate(int ledCount, int trees, int reducedVehicleKm) {
        // Each LED saves ~0.02 kWh/day vs incandescent; assume 22 days/month, 12 months
        BigDecimal energySavedKwh = BigDecimal.valueOf(ledCount).multiply(new BigDecimal("0.02")).multiply(new BigDecimal("264")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal carbonSavedEnergy = energySavedKwh.multiply(CarbonFactors.ELECTRICITY_KG_PER_KWH).setScale(2, RoundingMode.HALF_UP);
        BigDecimal moneySavedEnergy = energySavedKwh.multiply(new BigDecimal("8.0")).setScale(2, RoundingMode.HALF_UP);

        BigDecimal carbonAbsorbed = CarbonFactors.treeAbsorption(trees);
        BigDecimal carbonSavedTransport = BigDecimal.valueOf(reducedVehicleKm)
                .multiply(new BigDecimal("0.1")) // ~0.1 L/km
                .multiply(CarbonFactors.PETROL_KG_PER_LITRE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal moneySavedTransport = BigDecimal.valueOf(reducedVehicleKm).multiply(new BigDecimal("7.0")).setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalCarbonSaved = carbonSavedEnergy.add(carbonAbsorbed).add(carbonSavedTransport).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalMoneySaved = moneySavedEnergy.add(moneySavedTransport).setScale(2, RoundingMode.HALF_UP);

        return new SimResult(energySavedKwh, totalCarbonSaved, totalMoneySaved,
                carbonSavedEnergy, carbonAbsorbed, carbonSavedTransport, moneySavedEnergy, moneySavedTransport);
    }

    public static class SimResult {
        public final BigDecimal energySavedKwh, totalCarbonSaved, totalMoneySaved;
        public final BigDecimal carbonEnergy, carbonTrees, carbonTransport, moneyEnergy, moneyTransport;
        public SimResult(BigDecimal e, BigDecimal tc, BigDecimal tm, BigDecimal ce, BigDecimal ct, BigDecimal ctr, BigDecimal me, BigDecimal mt) {
            this.energySavedKwh = e; this.totalCarbonSaved = tc; this.totalMoneySaved = tm;
            this.carbonEnergy = ce; this.carbonTrees = ct; this.carbonTransport = ctr;
            this.moneyEnergy = me; this.moneyTransport = mt;
        }
    }
}
