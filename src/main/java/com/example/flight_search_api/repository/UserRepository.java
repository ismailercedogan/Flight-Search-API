package com.example.flight_search_api.repository;

import com.example.flight_search_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,long> {

    User findByUsername(String username);
}
