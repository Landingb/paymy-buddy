package com.oc.paymybuddy.services;

import com.oc.paymybuddy.domain.Transaction;
import com.oc.paymybuddy.utils.page.Paged;

import java.math.BigDecimal;
import java.util.Map;

public interface UserTransactionService {
    void create(Transaction transaction, Map<String, BigDecimal> feesMap);
    Paged<Transaction> getCurrentUserTransactionPage(int pageNumber, int size);
}
