    package com.example.ledgerlift.features.user;

    import com.example.ledgerlift.base.BasedMessage;
    import com.example.ledgerlift.domain.Role;
    import com.example.ledgerlift.domain.User;
    import com.example.ledgerlift.event.RegistrationCompleteEvent;
    import com.example.ledgerlift.features.mail.MailService;
    import com.example.ledgerlift.features.mail.verificationToken.VerificationToken;
    import com.example.ledgerlift.features.mail.verificationToken.VerificationTokenRepository;
    import com.example.ledgerlift.features.user.dto.RegistrationRequest;
    import com.example.ledgerlift.features.user.dto.RegistrationResponse;
    import com.example.ledgerlift.features.user.dto.UserResponse;
    import com.example.ledgerlift.features.user.dto.UserUpdateRequest;
    import com.example.ledgerlift.init.RoleRepository;
    import com.example.ledgerlift.mapper.UserMapper;
    import com.example.ledgerlift.utils.MailUtils;
    import com.example.ledgerlift.utils.Utils;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.context.ApplicationEventPublisher;
    import org.springframework.http.HttpStatus;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.server.ResponseStatusException;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    @RestController
    @RequestMapping("/api/v1/users")
    @RequiredArgsConstructor
    @Slf4j
    public class UserController {

        private final UserService userService;
        private final UserMapper userMapper;
        private final MailService mailService;
        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final ApplicationEventPublisher eventPublisher;
        private final VerificationTokenRepository verificationTokenRepository;

        @PostMapping("/user-registration")
        public BasedMessage createUser(@Valid @RequestBody RegistrationRequest request, final HttpServletRequest servletRequest) {

            UserResponse response = userService.createUser(request);
            User user = userMapper.fromUserResponse(response);

            List<Role> roles = new ArrayList<>();
            Role role = roleRepository.findByName("DONOR");
            roles.add(role);

            user.setRoles(roles);
            user.setCreatedAt(LocalDateTime.now());
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setDeleted(false);
            user.setIsBlocked(false);

            userRepository.save(user);

            VerificationToken token = new VerificationToken(MailUtils.generateDigitsToken(), VerificationToken.TokenType.EMAIL_VERIFICATION);
            token.setUser(user);
            verificationTokenRepository.save(token);

            user.setTokens(new ArrayList<>(List.of(token)));
            userRepository.save(user);

            log.info("User: {}", user);

            eventPublisher.publishEvent(new RegistrationCompleteEvent(user, Utils.getApplicationUrl(servletRequest)));

            return BasedMessage.builder()
                    .message("User has been registered, Please verify your email address")
                    .build();

        }

        @PostMapping("/verify-email")
        public RegistrationResponse verifyEmail(@RequestParam String token) {

            VerificationToken verificationToken = verificationTokenRepository.findByTokenAndType(token, VerificationToken.TokenType.EMAIL_VERIFICATION)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token has not been found"));

            User user = verificationToken.getUser();

            String validatedToken = userService.validateVerificationToken(verificationToken.getToken());

            if (validatedToken.equals("valid")) {
                user.setIsEmailVerified(true);
                userRepository.save(user);
                mailService.welcomeMessage(user);
                return RegistrationResponse.builder()
                        .message("Registration successfully")
                        .user(userMapper.toUserResponse(user))
                        .code(200)
                        .status(true)
                        .timeStamp(LocalDateTime.parse(LocalDateTime.now().toString()))
                        .data(user.getEmail())
                        .token(verificationToken.getToken())
                        .build();
            }
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Token has not been found"
            );
        }

        @GetMapping
        public List<UserResponse> getAllUsers() {
            return userService.getAllUsers();
        }

        @PutMapping("/{uuid}/upload-image")
        public BasedMessage uploadImage(@PathVariable String uuid, @RequestBody String profileImage) {

            userService.uploadProfile(uuid, profileImage);

            return BasedMessage.builder()
                    .message("Image uploaded successfully")
                    .build();

        }

        @GetMapping("/{uuid}")
        public UserResponse getUserByUuid(@PathVariable String uuid) {

            return userService.getUserByUuid(uuid);

        }

        @PutMapping("/{uuid}")
        public void updateUser(@PathVariable String uuid, @RequestBody UserUpdateRequest request) {

            userService.updateUser(uuid, request);

        }

        @PutMapping("/{uuid}/block-user")
        public void blockUser(@PathVariable String uuid) {

            userService.blockUserByUuid(uuid);

        }

        @PutMapping("/{uuid}/unblock-user")
        public BasedMessage unblockUser(@PathVariable String uuid) {

            userService.unblockUserByUuid(uuid);

            return BasedMessage.builder()
                    .message("User unblocked successfully")
                    .build();

        }

    }
