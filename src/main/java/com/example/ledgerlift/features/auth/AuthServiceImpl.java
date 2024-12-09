package com.example.ledgerlift.features.auth;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.auth.dto.AuthResponse;
import com.example.ledgerlift.features.auth.dto.LoginRequest;
import com.example.ledgerlift.features.auth.dto.RefreshTokenRequest;
import com.example.ledgerlift.features.auth.jwt_token.TokenService;
import com.example.ledgerlift.features.ca.CAService;
import com.example.ledgerlift.features.ca.dto.CAEnrollmentRequest;
import com.example.ledgerlift.features.user.UserRepository;
import com.example.ledgerlift.security.CustomUserDetails;
import com.example.ledgerlift.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Wallet;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final CAService caService;
    private Wallet wallet;

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User has not been found"
                        )
                );

        if (!user.getIsEmailVerified()) {
            if (user.getCreatedAt().isBefore(LocalDateTime.now().minusHours(24))) {
                userRepository.delete(user);
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "User has not been verified and has been deleted"
                );
            } else {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "User has not been verified yet"
                );
            }
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );

        auth = daoAuthenticationProvider.authenticate(auth);

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        try {

            CAEnrollmentRequest caEnrollmentRequest = CAEnrollmentRequest.builder()
                    .username("donor" + user.getUuid())
                    .affiliation("org1.department1")
                    .type("client")
                    .secret(Utils.generateUuid())
                    .orgName("Org1")
                    .genSecret(true)
                    .registrarUsername("admin")
                    .build();

            caService.registerAndEnrollUser(caEnrollmentRequest.getUsername(), caEnrollmentRequest);

        } catch (ResponseStatusException e) {
            // If the exception is related to already registered or enrolled user, allow the login to proceed
            if (!e.getMessage().contains("already enrolled")) {
                throw e;  // Rethrow if it's a different error
            }
            log.warn("User {} already enrolled, proceeding with login.", user.getUuid());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return tokenService.createToken(userDetails.getUser(), auth) ;
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {

        Authentication auth = new BearerTokenAuthenticationToken(
                request.refreshToken()
        );

        auth = jwtAuthenticationProvider.authenticate(auth);

        if (auth.getPrincipal() == null) {
            log.error("Authentication principal is null");
        } else {
            log.error("Authentication principal class: {} ", auth.getPrincipal().getClass().getName());
        }

        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized"
            );
        }

        Map<String, Object> claims = jwtAuth.getTokenAttributes();
        String authenticatedUuid = claims.get("uuid").toString();

        User user = userRepository.findByUuid(authenticatedUuid)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User has not been found"
                        )
                );

        return tokenService.createToken(user, auth);

    }
}
