package com.ai.users.services.implementation;

import com.ai.users.exceptions.ResourceNotFoundException;
import com.ai.users.io.request.InterviewResultSyncRequest;
import com.ai.users.io.request.QuestionLogRequest;
import com.ai.users.io.response.InterviewHistoryResponse;
import com.ai.users.io.response.SessionDetailResponse;
import com.ai.users.io.response.QuestionAnswerDTO;
import com.ai.users.models.*;
import com.ai.users.repositories.*;
import com.ai.users.services.InterviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewSessionRepository sessionRepository;
    private final InterviewQuestionRepository questionRepository;
    private final UserProfileRepository userRepository;
    private final ResumeRepository resumeRepository;

    @Override
    @Transactional
    public UUID createInterviewSession(UUID userId, UUID resumeId, String jd) {
        log.info("Creating new interview session for user: {} with resume: {}", userId, resumeId);

        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        InterviewSession session = InterviewSession.builder()
                .userProfile(user)
                .resume(resume)
                .jobDescription(jd)
                .status(SessionStatus.ONGOING)
                .build();

        return sessionRepository.save(session).getId();
    }

    @Override
    @Transactional
    public void logQuestionTurn(QuestionLogRequest request) {
        log.info("Logging Q&A turn for session: {}", request.getSessionId());

        InterviewSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        InterviewQuestion question = InterviewQuestion.builder()
                .session(session)
                .questionText(request.getQuestionText())
                .userResponse(request.getUserResponse())
                .analysis(request.getAnalysis())
                .build();

        questionRepository.save(question);
    }

    @Override
    @Transactional
    public void finalizeInterview(InterviewResultSyncRequest request) {
        log.info("Finalizing session: {} with score: {}", request.getSessionId(), request.getOverallScore());

        InterviewSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        session.setOverallScore(request.getOverallScore());
        session.setFeedbackReport(request.getFeedbackReport());
        session.setStatus(SessionStatus.COMPLETED);

        sessionRepository.save(session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewHistoryResponse> getUserInterviewHistory(UUID userId) {
        return sessionRepository.findByUserProfileIdOrderByStartedAtDesc(userId)
                .stream()
                .map(session -> InterviewHistoryResponse.builder()
                        .sessionId(session.getId())
                        .jobDescriptionSnippet(truncateJd(session.getJobDescription()))
                        .status(session.getStatus())
                        .score(session.getOverallScore())
                        .startedAt(session.getStartedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SessionDetailResponse getSessionDetails(UUID sessionId) {
        InterviewSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        List<QuestionAnswerDTO> transcript = questionRepository.findBySessionIdOrderByTimestampAsc(sessionId)
                .stream()
                .map(q -> QuestionAnswerDTO.builder()
                        .question(q.getQuestionText())
                        .answer(q.getUserResponse())
                        .analysis(q.getAnalysis())
                        .build())
                .collect(Collectors.toList());

        return SessionDetailResponse.builder()
                .sessionId(session.getId())
                .jobDescription(session.getJobDescription())
                .status(session.getStatus())
                .score(session.getOverallScore())
                .feedbackReport(session.getFeedbackReport())
                .transcript(transcript)
                .build();
    }

    private String truncateJd(String jd) {
        if (jd == null) return "";
        return jd.length() > 50 ? jd.substring(0, 47) + "..." : jd;
    }
}