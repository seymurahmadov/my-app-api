package com.company.myappapi.filter;

import com.querydsl.core.QueryModifiers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

public class SearchCriteria {
    private PageRequest pageRequest;
    private Map<String, Criteria> filters = new HashMap<>();

    public PageRequest getPageRequest() {
        return pageRequest;
    }

     public void setPageRequest(int pageNumber, int pageSize, Sort sort) {
        this.pageRequest = PageRequest.of(pageNumber, pageSize, sort);
    }


    public void setPageRequest(PageRequest pageRequest) {
        this.pageRequest = pageRequest;
    }

    public Map<String, Criteria> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Criteria> filters) {
        this.filters = filters;
    }

    public QueryModifiers getLimit() {
        return new QueryModifiers((long) pageRequest.getPageSize(), (long) pageRequest.getPageSize() * pageRequest.getPageNumber());
    }

    public Sort getSort() {
        return pageRequest.getSort().isEmpty() ? null : pageRequest.getSort();
    }

    public boolean sort() {
        return !pageRequest.getSort().isEmpty();
    }
}
