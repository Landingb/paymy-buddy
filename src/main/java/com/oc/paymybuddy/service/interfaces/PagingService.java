/**
 * 
 */
package com.oc.paymybuddy.service.interfaces;


import com.oc.paymybuddy.utils.page.Paging;


public interface PagingService {
	
	Paging of(int totalPages, int pageNumber);

}
