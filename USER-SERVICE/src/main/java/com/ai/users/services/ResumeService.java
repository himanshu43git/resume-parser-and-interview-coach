package com.ai.users.services;

import com.ai.users.io.request.ResumeSyncRequest;
import com.ai.users.io.response.ResumeResponse;
import java.util.List;
import java.util.UUID;

public interface ResumeService {

    // Called by Python Service to save NLP-parsed text
    ResumeResponse saveParsedResume(ResumeSyncRequest request);

    // Fetches all resumes for a specific user dashboard
    List<ResumeResponse> getAllResumesByUserId(UUID userId);

    // Gets details of a single resume
    ResumeResponse getResumeDetails(UUID resumeId);

    // Deletes a resume
    void deleteResume(UUID resumeId);
}