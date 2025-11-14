package com.company.myappapi.dao.user;


import com.company.myappapi.dto.response.activity.user.RoleResponseDto;
import com.company.myappapi.entity.user.Role;
import com.company.myappapi.exception.ResourceNotFoundException;
import com.company.myappapi.filter.Converter;
import com.company.myappapi.filter.SearchCriteria;
import com.company.myappapi.predicate.user.RolePredicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.company.myappapi.entity.user.QRole.role;


@Component
@Transactional
public class RoleDao {

    private final EntityManager manager;
    private final JPAQueryFactory query;

    public RoleDao(EntityManager manager, JPAQueryFactory query) {
        this.manager = manager;
        this.query = query;
    }

    public Role create(Role role) {
        manager.persist(role);
        return role;
    }

    @SuppressWarnings("deprecation")
    public Page<RoleResponseDto> getAll(SearchCriteria search) {
        var order = Converter.toOrderSpecifier(Role.class, search);
        var predicate = RolePredicate.find(search.getFilters());

        var list = query.select(Projections.constructor(RoleResponseDto.class, role.id, role.name))
                .from(role)
                .where(predicate)
                .orderBy(order)
                .restrict(search.getLimit())
                .fetch();

        long count = query.selectFrom(role)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(list,search.getPageRequest() ,count);
    }

    public void update(Role r) {
        manager.merge(r);
    }

    public Role findById(Long id) {
        return Optional.ofNullable(
                query.selectFrom(role)
                        .where(role.id.eq(id))
                        .fetchFirst()
        ).orElseThrow(() -> new ResourceNotFoundException(id));
    }
}