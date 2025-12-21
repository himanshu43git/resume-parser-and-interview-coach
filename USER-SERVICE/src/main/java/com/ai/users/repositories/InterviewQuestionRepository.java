package com.ai.users.repositories;

import com.ai.users.models.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, UUID> {
    // For Transcript: Fetch all Q&A pairs for a specific session in order
    List<InterviewQuestion> findBySessionIdOrderByTimestampAsc(UUID sessionId);
}