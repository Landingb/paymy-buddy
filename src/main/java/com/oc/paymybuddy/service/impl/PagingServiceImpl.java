package com.oc.paymybuddy.service.impl;

import com.oc.paymybuddy.service.interfaces.PagingService;
import com.oc.paymybuddy.utils.page.Paging;
import org.springframework.stereotype.Service;


@Service
public class PagingServiceImpl implements PagingService {

	@Override
	public Paging of(int totalPages, int pageNumber) {
		
		return Paging.of(totalPages, pageNumber);
	}

}
