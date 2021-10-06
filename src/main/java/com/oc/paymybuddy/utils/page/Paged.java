package com.oc.paymybuddy.utils.page;

import org.springframework.data.domain.Page;


public class Paged<T> {

    private Page<T> page;


    public Paged(Page<T> page, Paging of) {
    }
}
