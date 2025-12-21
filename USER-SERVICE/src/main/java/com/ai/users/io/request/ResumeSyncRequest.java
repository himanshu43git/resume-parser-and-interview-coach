package com.ai.users.io.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeSyncRequest {

    @NotBlank(message = "Auth User ID is required to link the resume")
    private String authUserId;

    @NotBlank(message = "File name cannot be empty")
    @Size(max = 255, message = "File name is too long")
    private String fileName;

    @NotBlank(message = "File URL is required for storage reference")
    private String fileUrl;

    @NotBlank(message = "Parsed content cannot be empty")
    @Size(min = 10, message = "Parsed content seems too short to be a valid resume")
    private String parsedContent; // The text extracted by Python NLP
}