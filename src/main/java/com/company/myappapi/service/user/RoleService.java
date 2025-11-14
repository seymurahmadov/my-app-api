package com.company.myappapi.service.user;

import com.company.myappapi.dao.user.RoleDao;
import com.company.myappapi.dto.request.user.RoleRequestDto;
import com.company.myappapi.dto.response.activity.user.PermissionForRoleResponseDto;
import com.company.myappapi.dto.response.activity.user.RoleForIdResponseDto;
import com.company.myappapi.dto.response.activity.user.RoleResponseDto;
import com.company.myappapi.entity.user.Permission;
import com.company.myappapi.entity.user.Role;
import com.company.myappapi.entity.user.activity.Module;
import com.company.myappapi.filter.SearchCriteria;
import com.company.myappapi.mapper.user.RoleMapper;
import com.company.myappapi.repository.PermissionRepository;
import com.company.myappapi.service.user.activity.ActivityLogService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleMapper roleMapper;
    private final RoleDao roleDao;
    private final ActivityLogService activityLogService;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleMapper roleMapper, RoleDao roleDao, ActivityLogService activityLogService, PermissionRepository permissionRepository) {
        this.roleMapper = roleMapper;
        this.roleDao = roleDao;
        this.activityLogService = activityLogService;
        this.permissionRepository = permissionRepository;
    }

    public String create(RoleRequestDto roleRequestDto) {
        Role role = roleMapper.toRole(roleRequestDto);
        role.setName(roleRequestDto.getName());
        roleDao.create(role);
        return "Successfully created";
    }

    public Page<RoleResponseDto> getAll(SearchCriteria search) {
     return roleDao.getAll(search);
    }

    public String update(RoleRequestDto roleRequestDto, Long id) {
        Role role = roleDao.findById(id);
        roleMapper.updateRole(roleRequestDto,role);
        roleDao.update(role);

        activityLogService.logUpdate(roleRequestDto, Module.ROLE);

        return "Successfully updated";
    }

    public void grantSysPermissions(Long roleId, List<Long> permissions) {
        var role = roleDao.findById(roleId);

        var perm = permissions.stream()
                .map(Permission::new)
                .collect(Collectors.toSet());

        role.setPermission(perm);
        roleDao.update(role);
    }

    public RoleForIdResponseDto findById(Long id) {
        Role role = roleDao.findById(id);
        Set<Long> selectedPermissionIds = role.getPermission().stream().map(Permission::getId).collect(Collectors.toSet());
        List<PermissionForRoleResponseDto> setPermissions = permissionRepository.findAll().stream().map(p -> new PermissionForRoleResponseDto(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        selectedPermissionIds.contains(p.getId())
                )).sorted(Comparator.comparing(PermissionForRoleResponseDto::getName))
                .toList();

        return new RoleForIdResponseDto(role.getId(),role.getName(),setPermissions, selectedPermissionIds);
    }
}
