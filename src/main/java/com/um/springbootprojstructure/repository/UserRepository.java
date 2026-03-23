package com.um.springbootprojstructure.repository;

import com.um.springbootprojstructure.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByPublicRef(String publicRef);
    boolean existsByPublicRef(String publicRef);
}
