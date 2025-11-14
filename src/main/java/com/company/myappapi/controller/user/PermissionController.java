package com.company.myappapi.controller.user;

import com.company.myappapi.dto.request.user.PermissionRequestDto;
import com.company.myappapi.dto.response.activity.user.PermissionResponseDto;
import com.company.myappapi.entity.user.Permission;
import com.company.myappapi.filter.Converter;
import com.company.myappapi.service.user.PermissionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping(produces = "application/json")
//    @PreAuthorize("hasAuthority('PERMISSION_LIST')")
    public ResponseEntity<Page<PermissionResponseDto>> findAll(@RequestParam Map<String, String> query) {
        return ResponseEntity.ok(permissionService.findAll(Converter.convert(query)));
    }

    @PostMapping
//    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    public ResponseEntity<String> create(@Valid @RequestBody PermissionRequestDto request) {
        return ResponseEntity.ok(permissionService.create(request));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('PERMISSION_BY_ID')")
    public ResponseEntity<PermissionResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.findById(id));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
    public ResponseEntity<String> update(@PathVariable Long id, @Valid @RequestBody PermissionRequestDto request) {
        return ResponseEntity.ok(permissionService.update(request, id));
    }

    @GetMapping("role-id/{roleId}")
//    @PreAuthorize("hasAuthority('ROLES_PERMISSION_UPDATE')")
    public ResponseEntity<List<Permission>> findByPermissionByRoleId(@PathVariable Long roleId) {
        return ResponseEntity.ok(permissionService.findPermissionByRoleId(roleId));
    }
}
