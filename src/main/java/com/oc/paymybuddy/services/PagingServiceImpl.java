package com.oc.paymybuddy.services;

import com.oc.paymybuddy.utils.page.Paging;
import org.springframework.stereotype.Service;

@Service
public class PagingServiceImpl implements PagingService {

    @Override
    public Paging of(int totalPages, int pageNumber) {
        return Paging.of(totalPages, pageNumber);
    }
}
