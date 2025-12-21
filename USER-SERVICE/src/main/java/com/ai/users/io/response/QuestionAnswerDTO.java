package com.ai.users.io.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionAnswerDTO {
    private String question;
    private String answer;
    private String analysis;
}