package com.samyak.api.repository;

import com.samyak.api.entity.User;
import com.samyak.api.util.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndStatus(Long id, UserStatus status);
}
