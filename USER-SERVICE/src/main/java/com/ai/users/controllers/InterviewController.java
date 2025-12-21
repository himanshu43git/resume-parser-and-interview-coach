package com.ai.users.controllers;

import com.ai.users.io.request.InterviewResultSyncRequest;
import com.ai.users.io.request.QuestionLogRequest;
import com.ai.users.io.response.InterviewHistoryResponse;
import com.ai.users.io.response.SessionDetailResponse;
import com.ai.users.services.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/interviews")
//@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/session")
    public ResponseEntity<UUID> startSession(@RequestParam UUID userId,
                                             @RequestParam UUID resumeId,
                                             @RequestBody String jd) {
        return ResponseEntity.ok(interviewService.createInterviewSession(userId, resumeId, jd));
    }

    @PostMapping("/log-turn")
    public ResponseEntity<Void> logTurn(@Valid @RequestBody QuestionLogRequest request) {
        interviewService.logQuestionTurn(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/finalize")
    public ResponseEntity<Void> finish(@Valid @RequestBody InterviewResultSyncRequest request) {
        interviewService.finalizeInterview(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<InterviewHistoryResponse>> getHistory(@PathVariable UUID userId) {
        return ResponseEntity.ok(interviewService.getUserInterviewHistory(userId));
    }

    @GetMapping("/details/{sessionId}")
    public ResponseEntity<SessionDetailResponse> getDetails(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(interviewService.getSessionDetails(sessionId));
    }
}