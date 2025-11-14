package com.company.myappapi.filter;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Converter {
    private static final String SORT = "sort";
    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_SIZE = "pageSize";
    private static final List<String> ignore = Arrays.asList(SORT, PAGE_NUMBER, PAGE_SIZE);

    private Converter() {
    }

    public static SearchCriteria convert(Map<String, String> query) {
        var searchCriteria = new SearchCriteria();
        searchCriteria.setPageRequest(sortingPagination(query));

        var list = query.entrySet()
                .stream()
                .filter(x -> !ignore.contains(x.getKey()) &&
                        x.getValue() != null &&
                        !x.getValue().isEmpty() &&
                        StringUtils.split(x.getValue(), ":").length > 1)
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> new Criteria(e.getKey(), e.getValue())
                ));

        searchCriteria.setFilters(list);

        return searchCriteria;
    }

    private static PageRequest sortingPagination(Map<String, String> query) {
        Sort sort = null;

        if (query.containsKey(SORT)) {
            var se = Arrays.stream(query.get(SORT).split(",")).collect(Collectors.toList());

            if (!se.isEmpty()) {
                var dir = se.get(se.size() - 1).equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
                sort = Sort.by(se.stream()
                        .filter(x -> !x.equalsIgnoreCase("asc")
                                && !x.equalsIgnoreCase("desc"))
                        .map(y -> new Sort.Order(dir, y))
                        .collect(Collectors.toList())
                );
            }
        }

        int pageNumber = 0;
        int pageSize = 10;

        if (query.containsKey(PAGE_NUMBER) && query.containsKey(PAGE_SIZE)) {
            pageNumber = Integer.parseInt(query.get(PAGE_NUMBER)) - 1;
            pageSize = Integer.parseInt(query.get(PAGE_SIZE));
        }

        return sort != null ? PageRequest.of(pageNumber, pageSize, sort) : PageRequest.of(pageNumber, pageSize);
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public static OrderSpecifier[] toOrderSpecifier(Class clazz, SearchCriteria src) {
        var orderByExpression = new PathBuilderFactory().create(clazz);
        var os = new HashSet<OrderSpecifier>();
        var byId = new OrderSpecifier(Order.DESC, orderByExpression.get("id"));

        if (src.sort()) {
            src.getSort().stream().findFirst().ifPresent(s -> {
                String property = s.getProperty();

                if (!property.equalsIgnoreCase("id")) {
                    os.add(byId);
                }
            });
        } else {
            os.add(byId);
        }

        return os.toArray(OrderSpecifier[]::new);
    }
}
