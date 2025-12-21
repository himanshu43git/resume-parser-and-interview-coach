package com.ai.users.io.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResultSyncRequest {
    private UUID sessionId;
    private Integer overallScore;
    private String feedbackReport;
}