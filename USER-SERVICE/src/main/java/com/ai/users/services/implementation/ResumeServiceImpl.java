package com.ai.users.services.implementation;

import com.ai.users.exceptions.ResourceNotFoundException;
import com.ai.users.io.request.ResumeSyncRequest;
import com.ai.users.io.response.ResumeResponse;
import com.ai.users.models.Resume;
import com.ai.users.models.UserProfile;
import com.ai.users.repositories.ResumeRepository;
import com.ai.users.repositories.UserProfileRepository;
import com.ai.users.services.ResumeService;
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
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserProfileRepository userRepository;

    @Override
    @Transactional
    public ResumeResponse saveParsedResume(ResumeSyncRequest request) {
        log.info("Syncing resume for authUserId: {}", request.getAuthUserId());

        // 1. Validate User existence via Auth ID
        UserProfile user = userRepository.findByAuthUserId(request.getAuthUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Auth ID: " + request.getAuthUserId()));

        // 2. Check for duplicate filename for this specific user
        if (resumeRepository.existsByFileNameAndUserProfile(request.getFileName(), user)) {
            log.warn("Resume with name {} already exists for this user. Updating existing record logic could go here.", request.getFileName());
            // Optionally: handle update logic or throw an exception.
            // For now, we will allow the save as a new record or you could throw a ConflictException.
        }

        // 3. Map Request DTO to Entity
        Resume resume = Resume.builder()
                .userProfile(user)
                .fileName(request.getFileName())
                .fileUrl(request.getFileUrl())
                .parsedContent(request.getParsedContent())
                .build();

        // 4. Persist and Map back to Response DTO
        Resume savedResume = resumeRepository.save(resume);
        return mapToResponse(savedResume);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeResponse> getAllResumesByUserId(UUID userId) {
        log.info("Fetching all resumes for user internal ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        return resumeRepository.findByUserProfileId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeResponse getResumeDetails(UUID resumeId) {
        log.info("Fetching details for resume ID: {}", resumeId);

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID: " + resumeId));

        return mapToResponse(resume);
    }

    @Override
    @Transactional
    public void deleteResume(UUID resumeId) {
        log.info("Deleting resume ID: {}", resumeId);

        if (!resumeRepository.existsById(resumeId)) {
            throw new ResourceNotFoundException("Cannot delete. Resume not found with ID: " + resumeId);
        }
        resumeRepository.deleteById(resumeId);
    }

    /**
     * Private helper to convert Entity to DTO.
     * Keeps the business logic clean and avoids code duplication.
     */
    private ResumeResponse mapToResponse(Resume resume) {
        return ResumeResponse.builder()
                .resumeId(resume.getId())
                .fileName(resume.getFileName())
                .fileUrl(resume.getFileUrl())
                .parsedContent(resume.getParsedContent())
                .uploadedAt(resume.getUploadedAt())
                .build();
    }
}