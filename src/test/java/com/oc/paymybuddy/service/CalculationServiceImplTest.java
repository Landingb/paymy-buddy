package com.oc.paymybuddy.service;
import com.oc.paymybuddy.service.impl.CalculationServiceImpl;
import com.oc.paymybuddy.service.interfaces.CalculationService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculationServiceImplTest {

    @Test
    void testAddTwoSameCurrencies() {
        // Arrange
        BigDecimal a = new BigDecimal("2.38");
        Currency cur1 = Currency.getInstance("EUR");
        BigDecimal b = new BigDecimal("3.54");
        Currency cur2 = Currency.getInstance("EUR");
        CalculationService calculationService = new CalculationServiceImpl();

        // Act
        BigDecimal result = calculationService.sumCurrencies(a, cur1, b, cur2);

        // Assert
        assertEquals(new BigDecimal("5.92"),result);
    }

    @Test
    void testAddTwoDifferentCurrencies() {
        // Arrange
        BigDecimal a = new BigDecimal("2.38");
        Currency cur1 = Currency.getInstance("EUR");
        BigDecimal b = new BigDecimal("3.54");
        Currency cur2 = Currency.getInstance("USD");
        CalculationServiceImpl calculationServiceImpl = new CalculationServiceImpl();

        Map<String, BigDecimal> mapCurConversion = new HashMap<>();
        mapCurConversion.put("USDEUR", new BigDecimal("0.82"));
        calculationServiceImpl.setCurrenciesConversionMap(mapCurConversion);

        // Act
        BigDecimal result = calculationServiceImpl.sumCurrencies(a, cur1, b, cur2);

        // Assert
        assertEquals(new BigDecimal("5.28"),result);
    }

    @Test
    void calculateFees_amount1fee1percent() {
        // Arrange
        BigDecimal initialAmount = new BigDecimal("-1");
        CalculationServiceImpl calculationServiceImpl = new CalculationServiceImpl();
        calculationServiceImpl.setFeeValue(new BigDecimal("0.01"));

        // Act
        Map<String, BigDecimal> feesMap = calculationServiceImpl.calculateFees(initialAmount);

        // Assert
        assertEquals(new BigDecimal("-0.01"),feesMap.get("fees"));
        assertEquals(new BigDecimal("-0.99"),feesMap.get("finalAmount"));
    }

    @Test
    void calculateFees_amount1fee0point5percent() {
        // Arrange
        BigDecimal initialAmount = new BigDecimal("-1");
        CalculationServiceImpl calculationServiceImpl = new CalculationServiceImpl();
        calculationServiceImpl.setFeeValue(new BigDecimal("0.001"));

        // Act
        Map<String, BigDecimal> feesMap = calculationServiceImpl.calculateFees(initialAmount);

        // Assert
        assertEquals(new BigDecimal("-0.01"),feesMap.get("fees"));
        assertEquals(new BigDecimal("-0.99"),feesMap.get("finalAmount"));
    }
}
