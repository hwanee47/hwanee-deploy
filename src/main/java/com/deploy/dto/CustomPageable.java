package com.deploy.dto;

import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
public class CustomPageable implements Pageable {

    private int page;
    private int perPage;

    public CustomPageable(int page, int perPage) {
        this.page = page;
        this.perPage = perPage;
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return perPage;
    }

    @Override
    public long getOffset() {
        return (long) (page - 1) * perPage;
    }

    @Override
    public Sort getSort() {
        return Sort.unsorted();
    }

    @Override
    public Pageable next() {
        return new CustomPageable(page + 1, perPage);
    }

    @Override
    public Pageable previousOrFirst() {
        return page == 1 ? this : new CustomPageable(page - 1, perPage);
    }

    @Override
    public Pageable first() {
        return new CustomPageable(1, perPage);
    }

    @Override
    public boolean hasPrevious() {
        return page > 1;
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new CustomPageable(pageNumber, perPage);
    }

    @Override
    public boolean isPaged() {
        return true;
    }

    @Override
    public boolean isUnpaged() {
        return false;
    }
}
