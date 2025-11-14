package com.company.myappapi.controller.user;

import com.company.myappapi.dto.request.user.UserRequestDto;
import com.company.myappapi.dto.response.activity.user.UserResponseDto;
import com.company.myappapi.entity.user.User;
import com.company.myappapi.filter.Converter;
import com.company.myappapi.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = "application/json")
//    @PreAuthorize("hasAuthority('USERS_LIST')")
    public ResponseEntity<Page<UserResponseDto>> findAll(@RequestParam Map<String, String> query) {
        return ResponseEntity.ok(userService.getAll(Converter.convert(query)));
    }


    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('USER_BY_ID')")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<String> update(@PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.update(userRequestDto, id));
    }

    @PutMapping("/{id}/disable-enable")
//    @PreAuthorize("hasAuthority('USER_DISABLE_ENABLE')")
    public ResponseEntity<String> disableEnable(@PathVariable Long id) {
        return ResponseEntity.ok(userService.disableEnable(id));
    }
}
