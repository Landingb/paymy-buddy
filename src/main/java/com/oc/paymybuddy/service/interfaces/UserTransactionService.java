package com.oc.paymybuddy.service.interfaces;

import com.oc.paymybuddy.domain.Transaction;
import com.oc.paymybuddy.utils.page.Paged;


import java.math.BigDecimal;
import java.util.Map;



public interface UserTransactionService {
	void create(Transaction transaction, Map<String, BigDecimal> feesMap);
	Paged<Transaction> getCurrentUserUserTransactionPage(int pageNumber, int size);
}
