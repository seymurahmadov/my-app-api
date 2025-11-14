package com.company.myappapi.dao.user;


import com.company.myappapi.dto.response.activity.user.UserResponseDto;
import com.company.myappapi.entity.user.User;
import com.company.myappapi.exception.ResourceNotFoundException;
import com.company.myappapi.filter.Converter;
import com.company.myappapi.filter.SearchCriteria;
import com.company.myappapi.predicate.user.UserPredicate;
import com.company.myappapi.repository.UserRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.company.myappapi.entity.user.QPermission.permission;
import static com.company.myappapi.entity.user.QRole.role;
import static com.company.myappapi.entity.user.QUser.user;


@Component
@Transactional
public class UserDao {

    private final EntityManager manager;
    private final JPAQueryFactory query;
    private final com.company.myappapi.repository.UserRepository userRepository;

    public UserDao(EntityManager manager, JPAQueryFactory query, UserRepository userRepository) {
        this.manager = manager;
        this.query = query;
        this.userRepository = userRepository;
    }

    public User create(User user) {
        manager.persist(user);
        return user;
    }

    public void update(User user) {
        manager.merge(user);
    }

    @SuppressWarnings("deprecation")
    public Page<UserResponseDto> findAll(SearchCriteria search) {
        var order = Converter.toOrderSpecifier(User.class, search);
        var predicate = UserPredicate.find(search.getFilters());

        var list = query.select(Projections.constructor(UserResponseDto.class,
                        user.id, user.name, user.surname, user.email,
                        user.active, user.createdAt, user.employmentDate,
                        role.id, role.name
                       ))
                .from(user)
                .leftJoin(user.role, role)
                .where(predicate)
                .orderBy(order)
                .restrict(search.getLimit())
                .fetch();

        long count = query.selectFrom(user)
                .leftJoin(user.role, role)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(list, search.getPageRequest(), count);
    }



    public Optional<User> findUser(String email) {
        return Optional.ofNullable(query.select(user)
                .from(user)
                .leftJoin(user.role, role).fetchJoin()
                .leftJoin(role.permission, permission).fetchJoin()
                .where(user.email.eq(email).and(user.active.eq(true))).fetchFirst());
    }

    public User findByEmail(String email) {
        return Optional.ofNullable(
                        query
                                .selectFrom(user)
                                .where(user.active.eq(true).and(user.email.eq(email)))
                                .fetchFirst())
                .orElseThrow(() -> new ResourceNotFoundException(email));
    }

    public User findById(Long id) {
        return Optional.ofNullable(
                query.selectFrom(user)
                        .where(user.id.eq(id))
                        .fetchFirst()
        ).orElseThrow(() -> new ResourceNotFoundException(id));
    }


}