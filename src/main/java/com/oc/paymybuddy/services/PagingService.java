package com.oc.paymybuddy.services;

public interface PagingService {
    Paging of(int totalPages, int pageNumber);
}
