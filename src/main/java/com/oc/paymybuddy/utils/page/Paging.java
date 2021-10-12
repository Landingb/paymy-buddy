package com.oc.paymybuddy.utils.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



@NoArgsConstructor
@AllArgsConstructor
public class Paging {

    private static final int PAGINATION_STEP = 3;

    private boolean nextEnabled;
    private boolean prevEnabled;
    private int pageNumber;

    private List<PageItem> items = new ArrayList<>();


    public void addPageItems(int from, int to, int pageNumber) {
        for (int i = from; i < to; i++) {
            items.add(PageItem.builder()
                    .active(pageNumber != i)
                    .index(i)
                    .pageItemType(PageItemType.PAGE)
                    .build());
        }
    }


    public void last(int totalPages) {
        items.add(PageItem.builder()
                .active(false)
                .pageItemType(PageItemType.DOTS)
                .build());

        items.add(PageItem.builder()
                .active(true)
                .index(totalPages)
                .pageItemType(PageItemType.PAGE)
                .build());
    }



    public void first() {
        items.add(PageItem.builder()
                .active(true)
                .index(1)
                .pageItemType(PageItemType.PAGE)
                .build());

        items.add(PageItem.builder()
                .active(false)
                .pageItemType(PageItemType.DOTS)
                .build());
    }



    public static Paging of(int totalPages, int pageNumber){
        Paging paging = new Paging();
        paging.setNextEnabled(pageNumber != totalPages); //Next enabled if not on last page
        paging.setPrevEnabled(pageNumber != 1); //Prev enabled if not on 1st page
        paging.setPageNumber(pageNumber);

        //In this case all pages will have links (no dots link):
        if (totalPages < PAGINATION_STEP * 2 + 6) {
            paging.addPageItems(1, totalPages + 1, pageNumber);
            //pageNumber around the beginning of pages so first pages will have links, next will have dots:
        } else if (pageNumber < PAGINATION_STEP * 2 + 1) { //totalpages>12 and pageNumber<7
            paging.addPageItems(1, PAGINATION_STEP * 2 + 4, pageNumber);//create pages until 10
            paging.last(totalPages);
            //pageNumber around the end of pages so first pages will have dots, last will have links:
        } else if (pageNumber > totalPages - PAGINATION_STEP * 2) { //totalpages>12 and pageNumber less than 6 from the end
            paging.first();
            paging.addPageItems(totalPages - PAGINATION_STEP * 2 - 2, totalPages + 1, pageNumber);
            //pageNumber is in the middle of pages, so first pages have dots, middle have links, last pages have dots.
        } else {
            paging.first();
            paging.addPageItems(pageNumber - PAGINATION_STEP, pageNumber + PAGINATION_STEP + 1, pageNumber);
            paging.last(totalPages);
        }

        return paging;
    }

    public static int getPaginationStep() {
        return PAGINATION_STEP;
    }

    public boolean isNextEnabled() {
        return nextEnabled;
    }

    public void setNextEnabled(boolean nextEnabled) {
        this.nextEnabled = nextEnabled;
    }

    public boolean isPrevEnabled() {
        return prevEnabled;
    }

    public void setPrevEnabled(boolean prevEnabled) {
        this.prevEnabled = prevEnabled;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<PageItem> getItems() {
        return items;
    }

    public void setItems(List<PageItem> items) {
        this.items = items;
    }
}