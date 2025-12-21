package com.ai.users.repositories;

import com.ai.users.models.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, UUID> {
    // For Dashboard: Fetch all sessions for a user, newest first
    List<InterviewSession> findByUserProfileIdOrderByStartedAtDesc(UUID userId);
}