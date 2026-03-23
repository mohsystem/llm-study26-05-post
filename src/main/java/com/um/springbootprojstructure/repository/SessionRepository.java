package com.um.springbootprojstructure.repository;

import com.um.springbootprojstructure.entity.Session;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findBySessionRef(String sessionRef);
}
