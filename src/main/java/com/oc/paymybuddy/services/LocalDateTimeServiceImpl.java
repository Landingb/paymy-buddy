package com.oc.paymybuddy.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LocalDateTimeServiceImpl implements LocalTimeService {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
