package com.oc.paymybuddy.services;

import com.oc.paymybuddy.domain.TransactionBank;
import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.repositories.TransactionBankRepository;
import com.oc.paymybuddy.utils.page.Paged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TransactionBankServiceImpl implements TransactionBankService {

    Logger logger = LoggerFactory.getLogger(TransactionBankServiceImpl.class);

    @Autowired
    private TransactionBankRepository transactionBankRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LocalTimeService localTimeService;

    @Autowired
    private PagingService pagingService;

    @Override
    public void create(TransactionBank transactionBank) {
        logger.debug("Calling create(TransactionBank");
        User currentUser = userService.getCurrentUser();

        transactionBank.setBankaccountnumber(currentUser.getBankaccountnumber());
        transactionBank.setDatetime(localTimeService.now());
        transactionBank.setUser(currentUser);

        transactionBankRepository.save(transactionBank);
    }

    @Override
    public void deleteAll() {
        transactionBankRepository.deleteAll();
    }

    @Override
    public Paged<TransactionBank> getCurrentUserTransactionBankPage(int pageNumber, int size) {

        PageRequest request = PageRequest.of(pageNumber-1, size, Sort.by(Sort.Direction.DESC,"id"));
        Page<TransactionBank> page = transactionBankRepository.findBankTransactionByUserId(userService.getCurrentUser().getId(),request);
        return new Paged<>(page, pagingService.of(page.getTotalPages(), pageNumber));
    }
}
