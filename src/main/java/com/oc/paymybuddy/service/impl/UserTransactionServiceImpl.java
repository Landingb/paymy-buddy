package com.oc.paymybuddy.service.impl;


import com.oc.paymybuddy.domain.Transaction;
import com.oc.paymybuddy.domain.User;
import com.oc.paymybuddy.repositories.UserTransactionRepository;
import com.oc.paymybuddy.service.interfaces.LocalDateTimeService;
import com.oc.paymybuddy.service.interfaces.PagingService;
import com.oc.paymybuddy.service.interfaces.UserService;
import com.oc.paymybuddy.service.interfaces.UserTransactionService;
import com.oc.paymybuddy.utils.page.Paged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;


@Service
public class UserTransactionServiceImpl implements UserTransactionService {

	Logger logger = LoggerFactory.getLogger(UserTransactionServiceImpl.class);

	@Autowired
	private UserTransactionRepository userTransactionRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private LocalDateTimeService localDateTimeServiceImpl;
	@Autowired
	private PagingService pagingService;
	
	@Override
	public void create(Transaction transaction, Map<String, BigDecimal> feesMap) {
		logger.debug("Calling create(UserTransaction UserTransaction)");
		User currentUser = userService.getCurrentUser();
		
		transaction.setDatetime(localDateTimeServiceImpl.now());
		transaction.setUserSource(currentUser);
		transaction.setFees(feesMap.get("fees"));
		transaction.setAmount(feesMap.get("finalAmount"));
		
		userTransactionRepository.save(transaction);
	}

	@Override
	public Paged<Transaction> getCurrentUserUserTransactionPage(int pageNumber, int size) {
		
		PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transaction> page = userTransactionRepository.findUserTransactionByUserId(userService.getCurrentUser().getId(),request);
        return new Paged<>(page, pagingService.of(page.getTotalPages(), pageNumber));
	}
}
