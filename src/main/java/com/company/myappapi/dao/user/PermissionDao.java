package com.company.myappapi.dao.user;

import com.company.myappapi.dto.response.activity.user.PermissionResponseDto;
import com.company.myappapi.entity.user.Permission;
import com.company.myappapi.exception.ResourceNotFoundException;
import com.company.myappapi.filter.Converter;
import com.company.myappapi.filter.SearchCriteria;
import com.company.myappapi.predicate.user.PermissionPredicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.company.myappapi.entity.user.QPermission.permission;
import static com.company.myappapi.entity.user.QRole.role;


@Component
@Transactional
public class PermissionDao {

    private final EntityManager manager;
    private final JPAQueryFactory query;

    public PermissionDao(EntityManager manager, JPAQueryFactory query) {
        this.manager = manager;
        this.query = query;
    }

    public Permission create(Permission permission) {
        manager.persist(permission);
        return permission;
    }

    @SuppressWarnings("deprecation")
    public Page<PermissionResponseDto> findAll(SearchCriteria search) {
        var order = Converter.toOrderSpecifier(Permission.class, search);
        var predicate = PermissionPredicate.find(search.getFilters());

        var list = query.select(Projections.constructor(PermissionResponseDto.class, permission.id, permission.name, permission.description))
                .from(permission)
                .where(predicate)
                .orderBy(order)
                .restrict(search.getLimit())
                .fetch();

        long count = query.selectFrom(permission)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(list, search.getPageRequest(), count);
    }

    public Permission findById(Long id) {
        return Optional.ofNullable(
                query.selectFrom(permission)
                        .where(permission.id.eq(id))
                        .fetchFirst()
        ).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void update(Permission permission) {
        manager.merge(permission);
    }

    public List<Permission> findRolePermissions(Long roleId) {
        return query.select(permission)
                .from(role)
                .leftJoin(role.permission, permission)
                .where(role.id.eq(roleId))
                .fetch()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}