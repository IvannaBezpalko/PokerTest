package com.test.poker.repository;

import com.test.poker.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    SessionEntity findByUuid(String uuid);
}
