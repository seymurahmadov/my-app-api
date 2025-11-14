package com.company.myappapi.controller.user;


import com.company.myappapi.dto.ChangePasswordDto;
import com.company.myappapi.dto.SignInRequestDto;
import com.company.myappapi.dto.SignInResponseDto;
import com.company.myappapi.dto.auth.AuthMeResponseDto;
import com.company.myappapi.dto.auth.ForgotPasswordDto;
import com.company.myappapi.dto.request.user.UserRequestDto;
import com.company.myappapi.exception.UserNotFoundException;
import com.company.myappapi.service.user.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> authenticateUser(@Valid @RequestBody SignInRequestDto requestDTO) throws UserNotFoundException {
        return ResponseEntity.ok(authService.signIn(requestDTO));
    }

    @PostMapping("/sign-up")
//    @PreAuthorize("hasAuthority('AUTH_SIGN_UP')")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(authService.signUp(dto));
    }

    @GetMapping("/me")
//    @PreAuthorize("hasAuthority('AUTH_GET_ME')")
    public ResponseEntity<AuthMeResponseDto> me() {
        return ResponseEntity.ok(authService.getMe());
    }

    @PutMapping("/change-password")
//    @PreAuthorize("hasAuthority('CHANGE_PASSWORD')")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto passwordDTO) {
        return ResponseEntity.ok(authService.changePassword(passwordDTO));
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordDto passwordDTO) {
        return ResponseEntity.ok(authService.forgotPassword(passwordDTO));
    }

    @PutMapping("/reset-password/{id}")
//    @PreAuthorize("hasAuthority('RESET_PASSWORD')")
    public ResponseEntity<String> resetPassword(@PathVariable Long id) {
        return ResponseEntity.ok(authService.resetPassword(id));
    }
}
