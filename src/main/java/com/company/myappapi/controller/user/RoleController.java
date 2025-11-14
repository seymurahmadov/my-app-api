package com.company.myappapi.controller.user;

import com.company.myappapi.dto.request.user.RoleRequestDto;
import com.company.myappapi.dto.response.activity.user.RoleForIdResponseDto;
import com.company.myappapi.dto.response.activity.user.RoleResponseDto;
import com.company.myappapi.filter.Converter;
import com.company.myappapi.service.user.RoleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(produces = "application/json")
//    @PreAuthorize("hasAuthority('ROLES_LIST')")
    public ResponseEntity<Page<RoleResponseDto>> getRoles(@RequestParam Map<String, String> query) {
        return ResponseEntity.ok(roleService.getAll(Converter.convert(query)));
    }

    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    public ResponseEntity<String> create(@Valid @RequestBody RoleRequestDto roleRequestDto) {
        return ResponseEntity.ok(roleService.create(roleRequestDto));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    public ResponseEntity<String> update(@Valid @RequestBody RoleRequestDto roleRequestDto, @PathVariable Long id) {
        return ResponseEntity.ok(roleService.update(roleRequestDto, id));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('ROLE_BY_ID')")
    public ResponseEntity<RoleForIdResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PutMapping("/{id}/sys-permissions")
//    @PreAuthorize("hasAuthority('SYS_PERMISSIONS_UPDATE')")
    public ResponseEntity<Void> grantSysPermissions(@PathVariable Long id, @RequestBody List<Long> permissions) {
        roleService.grantSysPermissions(id, permissions);
        return ResponseEntity.ok().build();
    }
}


