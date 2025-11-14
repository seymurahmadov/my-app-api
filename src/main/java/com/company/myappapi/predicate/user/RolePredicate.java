package com.company.myappapi.predicate.user;

import com.company.myappapi.filter.Criteria;
import com.company.myappapi.filter.FilterOperation;
import com.company.myappapi.predicate.PredicateAbstract;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Map;

import static com.company.myappapi.entity.user.QRole.role;


public class RolePredicate extends PredicateAbstract {

    public static Predicate find(Map<String, Criteria> filters) {
        var predicate = role.isNotNull();

        predicate = findByRole(predicate, filters);
        return predicate;
    }

    private static BooleanExpression findByRole(BooleanExpression predicate, Map<String, Criteria> filters) {
        if (filters.containsKey("name")) {
            Criteria src = filters.get("name");

            if (src.getOperation().equals(FilterOperation.EQUAL)) {
                var val = src.getSingleVal(String::valueOf);

                predicate = createOrAnd(predicate, role.name.eq(val));
            }
        }
        return predicate;
    }
}
