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
    private final AuthorityRepository authorityRepository;

    @PostConstruct
    void initRole() {

        // Auto-generate roles (ADMIN, DONOR, ORGANIZER)
        if (roleRepository.count() < 1) {

            // Authorities
            Authority donorRead = new Authority();
            donorRead.setName("donor:read");

            Authority donorWrite = new Authority();
            donorWrite.setName("donor:write");

            Authority adminRead = new Authority();
            adminRead.setName("admin:read");

            Authority adminWrite = new Authority();
            adminWrite.setName("admin:write");

            Authority organizerRead = new Authority();
            organizerRead.setName("organizer:read");

            Authority organizerWrite = new Authority();
            organizerWrite.setName("organizer:write");

            authorityRepository.saveAll(List.of(
               donorRead, donorWrite, adminRead, adminWrite, organizerRead, organizerWrite
            ));

            // Roles
            Role admin = new Role();
            admin.setName("ADMIN");
            admin.setAuthorities(List.of(
                    donorWrite, donorRead, adminRead, adminWrite, organizerRead, organizerWrite
            ));

            Role donor = new Role();
            donor.setName("DONOR");
            donor.setAuthorities(List.of(
                    donorWrite, donorRead
            ));

            Role organizer = new Role();
            organizer.setName("ORGANIZER");
            organizer.setAuthorities(List.of(
                    donorRead, organizerRead, organizerWrite
            ));

            // Save roles to repository
            roleRepository.saveAll(List.of(
                    admin, donor, organizer
            ));
        }
    }
}

