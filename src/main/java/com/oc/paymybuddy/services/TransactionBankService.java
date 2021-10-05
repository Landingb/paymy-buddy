package com.oc.paymybuddy.services;

import com.oc.paymybuddy.domain.TransactionBank;
import com.oc.paymybuddy.utils.page.Paged;


public interface TransactionBankService {
    void create (TransactionBank transactionBank);
    void deleteAll();
    Paged<TransactionBank> getCurrentUserTransactionBankPage(int pageNumber, int size);
}
