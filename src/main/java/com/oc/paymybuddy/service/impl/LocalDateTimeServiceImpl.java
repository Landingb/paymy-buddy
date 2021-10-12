package com.oc.paymybuddy.service.impl;

import com.oc.paymybuddy.service.interfaces.LocalDateTimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class LocalDateTimeServiceImpl implements LocalDateTimeService {
	
	@Override
	public LocalDateTime now() {
		return LocalDateTime.now();
	}

}
