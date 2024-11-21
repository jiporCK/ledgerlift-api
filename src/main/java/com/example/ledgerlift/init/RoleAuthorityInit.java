package com.example.ledgerlift.init;

import com.example.ledgerlift.domain.Authority;
import com.example.ledgerlift.domain.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleAuthorityInit {

    private final RoleRepository roleRepository;

    @PostConstruct
    void initRole() {

        // Auto-generate roles (ADMIN, DONOR, ORGANIZER)
        if (roleRepository.count() < 1) {

            // Authorities
            Authority readAuthority = new Authority();
            readAuthority.setName("read");

            Authority writeAuthority = new Authority();
            writeAuthority.setName("write");

            // Roles
            Role admin = new Role();
            admin.setName("ADMIN");
            admin.setAuthorities(List.of(
                    readAuthority,
                    writeAuthority
            ));

            Role donor = new Role();
            donor.setName("DONOR");
            donor.setAuthorities(List.of(
                    readAuthority,
                    writeAuthority
            ));

            Role organizer = new Role();
            organizer.setName("ORGANIZER");
            organizer.setAuthorities(List.of(
                    readAuthority,
                    writeAuthority
            ));

            // Save roles to repository
            roleRepository.saveAll(List.of(
                    admin, donor, organizer
            ));
        }
    }
}

