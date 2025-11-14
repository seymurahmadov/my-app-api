package com.company.myappapi.repository;

import com.company.myappapi.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUsersEntityByEmailAndActive(String username, Boolean active);

}
