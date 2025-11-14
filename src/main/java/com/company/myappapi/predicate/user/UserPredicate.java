package com.company.myappapi.predicate.user;

import com.company.myappapi.filter.Criteria;
import com.company.myappapi.filter.FilterOperation;
import com.company.myappapi.predicate.PredicateAbstract;
import com.company.myappapi.util.Util;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDate;
import java.util.Map;

import static com.company.myappapi.entity.user.QUser.user;


public class UserPredicate extends PredicateAbstract {
    public static Predicate find(Map<String, Criteria> filters) {
        var predicate = user.isNotNull();

        predicate = findById(predicate,filters);
        predicate = findByName(predicate, filters);
        predicate = findBySurname(predicate, filters);
        predicate = findByActive(predicate, filters);
        predicate = findByRole(predicate, filters);
        predicate = findByCreationDate(predicate, filters);
        predicate = findByEmploymentDate(predicate, filters);
        predicate = findByEmail(predicate, filters);

        return predicate;
    }

    private static BooleanExpression findById(BooleanExpression predicate, Map<String, Criteria> filters) {
        if (filters.containsKey("id")) {
            var src = filters.get("id");
            if (src.getOperation().equals(FilterOperation.EQUAL)) {
                var val = src.getSingleVal(Long::valueOf);
                return createOrAnd(predicate, user.id.eq(val));
            }
        }
        return predicate;
    }

    private static BooleanExpression findByEmail(BooleanExpression predicate, Map<String, Criteria> filters) {
        if (filters.containsKey("email")) {
            var src = filters.get("email");
            if (src.getOperation().equals(FilterOperation.CONTAINS)) {
                return createOrAnd(predicate, user.email.contains(src.getSingleVal(String::toString)));
            }
        }
        return predicate;
    }

    private static BooleanExpression findByName(BooleanExpression predicate, Map<String, Criteria> filters) {
        if (filters.containsKey("name")) {
            Criteria src = filters.get("name");
            if (src.getOperation().equals(FilterOperation.CONTAINS)) {
                return createOrAnd(predicate, user.name.contains(src.getSingleVal(String::toString)));
            }
        }
        return predicate;
    }

    private static BooleanExpression findBySurname(BooleanExpression predicate, Map<String, Criteria> filters) {
        if (filters.containsKey("surname")) {
            Criteria src = filters.get("surname");
            if (src.getOperation().equals(FilterOperation.CONTAINS)) {
                return createOrAnd(predicate, user.surname.contains(src.getSingleVal(String::toString)));
            }
        }
        return predicate;
    }

    private static BooleanExpression findByActive(BooleanExpression predicate, Map<String, Criteria> filters) {
        if (filters.containsKey("active")) {
            var src = filters.get("active");
            if (src.getOperation().equals(FilterOperation.EQUAL)) {
                return createOrAnd(predicate, user.active.eq(Boolean.valueOf(src.getSingleVal(String::toString))));
            }
        }
        return predicate;
    }

    private static BooleanExpression findByCreationDate(BooleanExpression predicate, Map<String, Criteria> filters) {
        if (filters.containsKey("createdAt")) {
            var src = filters.get("createdAt");
            if (src.getOperation().equals(FilterOperation.BETWEEN)) {
                var startOfTime = Util.startOfTime(src.getMinVal(LocalDate::parse));
                var endOfTime = Util.endOfTime(src.getMaxVal(LocalDate::parse));

                return createOrAnd(predicate, user.createdAt.goe(startOfTime)).and(user.createdAt.loe(endOfTime));
            }
        }
        return predicate;
    }

    private static BooleanExpression findByEmploymentDate(BooleanExpression predicate, Map<String, Criteria> filters) {
        if (filters.containsKey("employmentDate")) {
            var src = filters.get("employmentDate");
            if (src.getOperation().equals(FilterOperation.BETWEEN)) {
                LocalDate startOfTime = src.getMinVal(LocalDate::parse);
                LocalDate endOfTime = src.getMaxVal(LocalDate::parse);

                return createOrAnd(predicate, user.employmentDate.goe(startOfTime)).and(user.employmentDate.loe(endOfTime));
            }
        }
        return predicate;
    }

    private static BooleanExpression findByRole(BooleanExpression predicate, Map<String, Criteria> filters) {
        if (filters.containsKey("roleName")) {
            Criteria src = filters.get("roleName");

            if (src.getOperation().equals(FilterOperation.EQUAL)) {
                var val = src.getSingleVal(String::valueOf);

                predicate = createOrAnd(predicate, user.role.name.eq(val));
            }
        }
        return predicate;
    }
}