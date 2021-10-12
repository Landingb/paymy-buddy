package com.oc.paymybuddy.service.impl;


import com.oc.paymybuddy.domain.BankTransaction;
import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.repositories.BankTransactionRepository;
import com.oc.paymybuddy.service.interfaces.BankTransactionService;
import com.oc.paymybuddy.service.interfaces.LocalDateTimeService;
import com.oc.paymybuddy.service.interfaces.PagingService;
import com.oc.paymybuddy.service.interfaces.UserService;
import com.oc.paymybuddy.utils.page.Paged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BankTransactionServiceImpl implements BankTransactionService {

	Logger logger = LoggerFactory.getLogger(BankTransactionServiceImpl.class);

	@Autowired
	private BankTransactionRepository bankTransactionRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private LocalDateTimeService localDateTimeServiceImpl;
	@Autowired
	private PagingService pagingService;

	@Override
	public void create(BankTransaction bankTransaction) {
		logger.debug("Calling create(BankTransaction BankTransaction)");
		User currentUser = userService.getCurrentUser();

		bankTransaction.setBankaccountnumber(currentUser.getBankaccountnumber());
		bankTransaction.setDatetime(localDateTimeServiceImpl.now());
		bankTransaction.setUser(currentUser);

		bankTransactionRepository.save(bankTransaction);
	}

	@Override
	public Paged<BankTransaction> getCurrentUserBankTransactionPage(int pageNumber, int size) {

		PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<BankTransaction> page = bankTransactionRepository.findBankTransactionByUserId(userService.getCurrentUser().getId(),request);
		return new Paged<>(page, pagingService.of(page.getTotalPages(), pageNumber));

	}

	@Override
	public void deleteAll() {
		bankTransactionRepository.deleteAll();
	}


}

