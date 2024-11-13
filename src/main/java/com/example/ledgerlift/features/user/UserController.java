package com.example.ledgerlift.features.user;

import com.example.ledgerlift.domain.User;
import com.example.ledgerlift.features.user.userdto.UserCreateRequest;
import com.example.ledgerlift.features.user.userdto.UserResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequest request) {

        return ResponseEntity.ok()
                .body(userService.createUser(request));

    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

}
