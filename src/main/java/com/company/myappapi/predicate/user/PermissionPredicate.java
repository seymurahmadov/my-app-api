package com.company.myappapi.predicate.user;

import com.company.myappapi.filter.Criteria;
import com.company.myappapi.predicate.PredicateAbstract;
import com.querydsl.core.types.Predicate;

import java.util.Map;

import static com.company.myappapi.entity.user.QPermission.permission;


public class PermissionPredicate extends PredicateAbstract {

    public static Predicate find(Map<String, Criteria> filters) {
        var predicate = permission.isNotNull();

        return predicate;
    }
}
