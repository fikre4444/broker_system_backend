package com.broker.axumawit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.broker.axumawit.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
