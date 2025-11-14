package com.company.myappapi.predicate;

import com.querydsl.core.types.dsl.BooleanExpression;

public abstract class PredicateAbstract {
    public static BooleanExpression createOrAnd(BooleanExpression left, BooleanExpression right) {
        return left == null ? right : left.and(right);
    }
}
