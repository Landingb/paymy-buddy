package com.oc.paymybuddy.services;

import com.oc.paymybuddy.utils.page.Paging;

public interface PagingService {
    Paging of(int totalPages, int pageNumber);
}
