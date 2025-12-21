package com.ai.users.io.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ResumeResponse {
    private UUID resumeId;
    private String fileName;
    private String fileUrl;
    private String parsedContent;
    private LocalDateTime uploadedAt;
}