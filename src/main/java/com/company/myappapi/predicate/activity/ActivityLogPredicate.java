package com.company.myappapi.predicate.activity;

import com.company.myappapi.filter.Criteria;
import com.company.myappapi.filter.FilterOperation;
import com.company.myappapi.predicate.PredicateAbstract;
import com.querydsl.core.types.Predicate;

import java.util.Map;

import static com.company.myappapi.entity.user.activity.QActivityLog.activityLog;


public class ActivityLogPredicate extends PredicateAbstract {
    public static Predicate find(Map<String, Criteria> filters) {
        var predicate = activityLog.isNotNull();

        if (filters.containsKey("email")) {
            Criteria src = filters.get("email");

            if (src.getOperation().equals(FilterOperation.CONTAINS))
                predicate = createOrAnd(predicate, activityLog.user.email.containsIgnoreCase(src.getSingleVal(String::toString)));
        }

        return predicate;
    }
}
