package com.ai.users.io.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLogRequest {
    private UUID sessionId;      // Which interview session this belongs to
    private String questionText; // What the AI asked
    private String userResponse; // What the user said
    private String analysis;     // Optional: AI's immediate feedback on this specific answer
}