package com.ai.users.io.response;

import com.ai.users.models.SessionStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InterviewHistoryResponse {
    private UUID sessionId;
    private String jobDescriptionSnippet;
    private SessionStatus status;
    private Integer score;
    private LocalDateTime startedAt;
}