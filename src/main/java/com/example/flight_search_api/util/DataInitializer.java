package com.example.flight_search_api.util;

import javax.annotation.PostConstruct;

import com.example.flight_search_api.model.Role;
import com.example.flight_search_api.repository.RoleRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    @Transactional
    public void initialize() {
        initializeRoles();
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setId(1L);
            userRole.setName("USER");
            Role adminRole = new Role();
            adminRole.setId(2L);
            adminRole.setName("ADMIN");
            roleRepository.save(userRole);
            roleRepository.save(adminRole);
        }
    }
}
