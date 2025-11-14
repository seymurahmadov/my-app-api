package com.company.myappapi.service.user;


import com.company.myappapi.dao.user.RoleDao;
import com.company.myappapi.dao.user.UserDao;
import com.company.myappapi.dto.request.user.UserRequestDto;
import com.company.myappapi.dto.response.activity.user.UserResponseDto;
import com.company.myappapi.entity.user.User;
import com.company.myappapi.filter.SearchCriteria;
import com.company.myappapi.mapper.user.UserMapper;
import com.company.myappapi.service.user.activity.ActivityLogService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.company.myappapi.entity.user.activity.Module;

import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final ActivityLogService activityLogService;

    public UserService(UserMapper userMapper, UserDao userDao, RoleDao roleDao, ActivityLogService activityLogService) {
        this.userMapper = userMapper;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.activityLogService = activityLogService;
    }

    public Page<UserResponseDto> getAll(SearchCriteria search) {
        return userDao.findAll(search);
    }

    public String update(UserRequestDto userRequestDto, Long id) {
        User user = userDao.findById(id);
        userMapper.updateUser(userRequestDto, user);
        userDao.update(user);

        activityLogService.logUpdate(userRequestDto, Module.USER);

        return "Successfully updated user";
    }

    public User findById(Long id) {
       return userDao.findById(id);
//        return userMapper.toUserResponseDto(userDao.findById(id));
    }


    public Optional<User> findUser(String email) {
        return userDao.findUser(email);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public String disableEnable(Long id) {
        var user = userDao.findById(id);
        user.setActive(!user.getActive());
        userDao.update(user);

        activityLogService.logDelete(user, Module.USER);
        return "User status changed to " + user.getActive();
    }


}
