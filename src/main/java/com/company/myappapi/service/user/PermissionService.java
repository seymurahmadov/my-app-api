package com.company.myappapi.service.user;


import com.company.myappapi.dao.user.PermissionDao;
import com.company.myappapi.dto.request.user.PermissionRequestDto;
import com.company.myappapi.dto.response.activity.user.PermissionResponseDto;
import com.company.myappapi.entity.user.Permission;
import com.company.myappapi.entity.user.activity.Module;
import com.company.myappapi.filter.SearchCriteria;
import com.company.myappapi.mapper.user.PermissionMapper;
import com.company.myappapi.service.user.activity.ActivityLogService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionDao permissionDao;
    private final PermissionMapper permissionMapper;
    private final ActivityLogService activityLogService;


    public PermissionService(PermissionDao permissionDao, PermissionMapper permissionMapper, ActivityLogService activityLogService) {
        this.permissionDao = permissionDao;
        this.permissionMapper = permissionMapper;
        this.activityLogService = activityLogService;
    }

    public Page<PermissionResponseDto> findAll(SearchCriteria search) {
        return permissionDao.findAll(search);
    }

    public String create(PermissionRequestDto requestDto) {
        permissionDao.create(permissionMapper.toPermission(requestDto));
        return "Permission created successfully";
    }

    public String update(PermissionRequestDto requestDto, Long id) {
        Permission permission = permissionDao.findById(id);
        permissionMapper.updatePermission(requestDto, permission);
        permissionDao.update(permission);

        activityLogService.logUpdate(requestDto, Module.PERMISSION);

        return "Permission updated successfully";
    }

    public PermissionResponseDto findById(Long id) {
        return permissionMapper.toPermissionResponseDto(permissionDao.findById(id));
    }

    public List<Permission> findPermissionByRoleId(Long roleId) {
      return permissionDao.findRolePermissions(roleId);
    }
}
