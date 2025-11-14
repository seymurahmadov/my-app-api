package com.company.myappapi.service.user;

import com.company.myappapi.dao.user.RoleDao;
import com.company.myappapi.dao.user.UserDao;
import com.company.myappapi.dto.ChangePasswordDto;
import com.company.myappapi.dto.SignInRequestDto;
import com.company.myappapi.dto.SignInResponseDto;
import com.company.myappapi.dto.auth.*;
import com.company.myappapi.dto.request.user.UserRequestDto;
import com.company.myappapi.entity.user.EmailOutbox;
import com.company.myappapi.entity.user.Permission;
import com.company.myappapi.entity.user.User;
import com.company.myappapi.entity.user.activity.Module;
import com.company.myappapi.enumaration.EmailStatus;
import com.company.myappapi.exception.GenerateException;

import com.company.myappapi.exception.UserNotFoundException;
import com.company.myappapi.mapper.user.UserMapper;
import com.company.myappapi.repository.RoleRepository;
import com.company.myappapi.repository.UserRepository;
import com.company.myappapi.repository.outbox.EmailOutboxRepository;
import com.company.myappapi.security.CustomUserDetailsService;
import com.company.myappapi.security.JwtTokenUtil;
import com.company.myappapi.security.UserUtil;
import com.company.myappapi.service.user.activity.ActivityLogService;
import com.company.myappapi.util.EmailTemplateUtil;
import com.company.myappapi.util.Util;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Slf4j
@Service
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepo;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService jwtInMemoryUserDetailsService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RoleRepository roleRepository;
    private final EmailOutboxRepository emailOutboxRepository;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;
    private final ActivityLogService activityLogService;
    private final UserService userService;
    private JPAQueryFactory queryFactory;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepo, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, UserDetailsService jwtInMemoryUserDetailsService, CustomUserDetailsService customUserDetailsService, RoleRepository roleRepository, EmailOutboxRepository emailOutboxRepository, UserDao userDao, RoleDao roleDao, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper, ActivityLogService activityLogService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.jwtInMemoryUserDetailsService = jwtInMemoryUserDetailsService;
        this.customUserDetailsService = customUserDetailsService;
        this.roleRepository = roleRepository;
        this.emailOutboxRepository = emailOutboxRepository;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        this.activityLogService = activityLogService;
        this.userService = userService;
    }

    public String signUp(UserRequestDto dto) {
        Optional<User> entity = userDao.findUser(dto.getEmail());
        String password = Util.generateUserPassword();

        if (entity.isEmpty()) {
            User user = userMapper.toUser(dto);
            user.setRole(roleDao.findById(dto.getRoleId()));
            user.setPassword(passwordEncoder.encode(password));
            userDao.create(user);

//            EmailOutbox emailOutbox = new EmailOutbox();
//            emailOutbox.setReceiverEmail(user.getEmail());
//            emailOutbox.setSubject("KPI - Yeni hesab");
//            emailOutbox.setBody(EmailTemplateUtil.buildSignUpEmailBody(user.getEmail(), password, url));
//            emailOutbox.setStatus(EmailStatus.PENDING);
//            emailOutboxRepository.save(emailOutbox);

            activityLogService.logInsert(dto, Module.AUTH);

            return "You signed! Your password: " + password;
        } else return "This account already exist in our DB!";
    }


    public SignInResponseDto signIn(SignInRequestDto request) throws UserNotFoundException {
        try {
            authenticate(request.getEmail(), request.getPassword());
        } catch (Exception e) {
            throw new UserNotFoundException("username.or.password.incorrect");
        }

        final UserDetails userDetails = customUserDetailsService
                .loadUserByUsername(request.getEmail());

        User usersEntityByEmail = userRepo.findUsersEntityByEmailAndActive(userDetails.getUsername(), true);

        final String token = jwtTokenUtil.generateToken(userDetails);

        return SignInResponseDto.builder()
                .id(usersEntityByEmail.getId())
                .token(token)
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public String forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        var user = userService.findByEmail(forgotPasswordDto.getEmail());

        String newPassword = Util.generateUserPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.update(user);

//        EmailOutbox emailOutbox = new EmailOutbox();
//        emailOutbox.setReceiverEmail(forgotPasswordDto.getEmail());
//        emailOutbox.setSubject("KPI Yeni şifrə");
//        emailOutbox.setBody(EmailTemplateUtil.buildForgotPasswordEmailBody(user.getEmail(), newPassword, " "));
//        emailOutbox.setStatus(EmailStatus.PENDING);
//        emailOutboxRepository.save(emailOutbox);

//        activityLogService.logUpdate(forgotPasswordDto, Module.AUTH);

        return "An email has been sent to set a new password";
    }


    public String resetPassword(Long id) {
        var user = userDao.findById(id);
        var password = Util.generateUserPassword();

        user.setPassword(passwordEncoder.encode(password));
        userDao.update(user);

//        EmailOutbox emailOutbox = new EmailOutbox();
//        emailOutbox.setReceiverEmail(user.getEmail());
//        emailOutbox.setSubject("KPI Şifrə yenilənməsi");
//        emailOutbox.setBody(EmailTemplateUtil.buildResetPasswordEmailBody(user.getEmail(), password, "url"));
//        emailOutbox.setStatus(EmailStatus.PENDING);
//        emailOutboxRepository.save(emailOutbox);

        activityLogService.logUpdate(user, Module.AUTH);

        return "Password has been successfully reset. Check your email address.";
    }

    public String changePassword(ChangePasswordDto changePasswordDto) {

        var user = UserUtil.getCurrentUser();
        var currentUser = userDao.findById(user.getId());

        System.out.println(bCryptPasswordEncoder.matches(changePasswordDto.getOldPassword(), currentUser.getPassword()));

        if (!bCryptPasswordEncoder.matches(changePasswordDto.getOldPassword(), currentUser.getPassword())) {
            throw new GenerateException("user.password.not.match");
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new GenerateException("user.new.and.confirm.password.not.match");
        }

        currentUser.setPassword(bCryptPasswordEncoder.encode(changePasswordDto.getConfirmPassword()));
        userDao.update(currentUser);

//        activityLogService.logUpdate(changePasswordDto, Module.AUTH);

        return "Password has been successfully changed";
    }

    public AuthMeResponseDto getMe() {
        Set<String> permissionsSet = new HashSet<>();

        var user = UserUtil.getCurrentUser();
        var currentUser = userDao.findById(user.getId());

        Set<Permission> permissions = currentUser.getRole().getPermission();
        permissions.forEach(per -> permissionsSet.add(per.getName().toUpperCase()));
        AuthMeResponseDto authMeResponseDto = new AuthMeResponseDto();
        authMeResponseDto.setId(currentUser.getId());
        authMeResponseDto.setEmail(currentUser.getEmail());
        authMeResponseDto.setRoleName(currentUser.getRole().getName());
        authMeResponseDto.setPermissions(permissionsSet);
        return authMeResponseDto;
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}