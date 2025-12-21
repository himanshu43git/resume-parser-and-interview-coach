package com.ai.users.io.response;

import com.ai.users.models.SessionStatus;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class SessionDetailResponse {
    private UUID sessionId;
    private String jobDescription;
    private SessionStatus status;
    private Integer score;
    private String feedbackReport;
    private List<QuestionAnswerDTO> transcript;
}

