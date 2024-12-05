package com.example.ledgerlift.features.user.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserDetailResponse (

        String email,

        String firstName,

        String lastName,

        String username,

        String avatar,

        String createdAt,

        Boolean isBlocked,

        Boolean isEmailVerified,

        LocalDateTime lastLoginAt,

        List<RoleResponse> role

) {
}
