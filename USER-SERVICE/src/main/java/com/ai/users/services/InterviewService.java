package com.ai.users.services;

import com.ai.users.io.request.InterviewResultSyncRequest;
import com.ai.users.io.request.QuestionLogRequest;
import com.ai.users.io.response.InterviewHistoryResponse;
import com.ai.users.io.response.SessionDetailResponse;
import java.util.List;
import java.util.UUID;

public interface InterviewService {

    // Initializes a new session record
    UUID createInterviewSession(UUID userId, UUID resumeId, String jd);

    // Logs a single Q&A turn (from AI Service/Frontend)
    void logQuestionTurn(QuestionLogRequest request);

    // Updates session with final score and report
    void finalizeInterview(InterviewResultSyncRequest request);

    // Gets all past interviews for a user
    List<InterviewHistoryResponse> getUserInterviewHistory(UUID userId);

    // Gets the full transcript and report for one session
    SessionDetailResponse getSessionDetails(UUID sessionId);
}