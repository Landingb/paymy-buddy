package com.oc.paymybuddy.services;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public interface CalculationService {

    BigDecimal sumCurrencies(BigDecimal amount, Currency currency, BigDecimal amountToSum,
                             Currency currencyOfAmountToSum);

    Map<String, BigDecimal> calculateFees(BigDecimal initialAmount);
}
