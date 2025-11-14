package com.company.myappapi.service.user.activity;


import com.company.myappapi.dto.response.activity.ActivityLogResponse;
import com.company.myappapi.entity.user.activity.ActivityLog;
import com.company.myappapi.entity.user.activity.ActivityType;
import com.company.myappapi.filter.Converter;
import com.company.myappapi.filter.SearchCriteria;
import com.company.myappapi.predicate.activity.ActivityLogPredicate;
import com.company.myappapi.repository.activity.ActivityLogRepository;
import com.company.myappapi.security.UserUtil;
import com.company.myappapi.util.Util;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.company.myappapi.entity.user.activity.Module;

import static com.company.myappapi.entity.user.QUser.user;
import static com.company.myappapi.entity.user.activity.QActivityLog.activityLog;

@Service
@Transactional
@AllArgsConstructor
public class ActivityLogService {

    JPAQueryFactory queryFactory;
    ActivityLogRepository activityLogRepository;

    public void logInsert(Object object, Module module) {
        var body = Util.toJson(object);
        var user = UserUtil.getUser();

        activityLogRepository.save(ActivityLog.of(body, ActivityType.INSERT, module, user));
    }

    public void logUpdate(Object object, com.company.myappapi.entity.user.activity.Module module) {
        var body = Util.toJson(object);
        var user = UserUtil.getUser();

        activityLogRepository.save(ActivityLog.of(body, ActivityType.UPDATE, module, user));
    }

    public void logDelete(Object object, Module module) {
        var body = Util.toJson(object);
        var user = UserUtil.getUser();

        activityLogRepository.save(ActivityLog.of(body, ActivityType.DELETE, module, user));
    }

    @Transactional(readOnly = true)
    public Page<ActivityLogResponse> findAllByPage(SearchCriteria searchCriteria) {
        var predicate = ActivityLogPredicate.find(searchCriteria.getFilters());
        var order = Converter.toOrderSpecifier(ActivityLog.class, searchCriteria);

        var list = queryFactory.select(Projections.constructor(ActivityLogResponse.class,
                        activityLog.id,
                        activityLog.action,
                        activityLog.module,
                        activityLog.body,
                        Expressions.stringTemplate("concat({0}, ' ', {1})", user.name, user.surname),
                        activityLog.actionDate))
                .from(activityLog)
                .leftJoin(activityLog.user, user)
                .where(predicate)
                .orderBy(order)
                .restrict(searchCriteria.getLimit())
                .fetch();

        long count = queryFactory.selectFrom(activityLog)
                .leftJoin(activityLog.user, user)
                .where(predicate)
                .fetch()
                .size();

        return new PageImpl<>(list, searchCriteria.getPageRequest(), count);
    }
}