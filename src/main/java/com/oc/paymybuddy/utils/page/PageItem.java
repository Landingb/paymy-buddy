package com.oc.paymybuddy.utils.page;

import lombok.*;



@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PageItem {


    private PageItemType pageItemType;

    private int index;

    private boolean active;

    public PageItemType getPageItemType() {
        return pageItemType;
    }

    public void setPageItemType(PageItemType pageItemType) {
        this.pageItemType = pageItemType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}