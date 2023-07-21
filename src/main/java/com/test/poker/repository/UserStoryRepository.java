package com.test.poker.repository;

import com.test.poker.entity.UserStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoryRepository extends JpaRepository<UserStoryEntity, Long> {
}
