package com.oc.paymybuddy.service.interfaces;

import com.oc.paymybuddy.domain.BankTransaction;
import com.oc.paymybuddy.utils.page.Paged;


public interface BankTransactionService {
	void create(BankTransaction bankTransaction);
	void deleteAll();
	Paged<BankTransaction> getCurrentUserBankTransactionPage(int pageNumber, int size);
	
}
