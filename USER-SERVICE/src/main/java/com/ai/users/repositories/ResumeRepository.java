package com.ai.users.repositories;

import com.ai.users.models.Resume;
import com.ai.users.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, UUID> {

    // Basic Lookups
    List<Resume> findByUserProfile(UserProfile userProfile);

    // Finds all resumes for a user using their internal UUID
    List<Resume> findByUserProfileId(UUID userId);

    // Validation & Security
    // Prevents the same user from uploading the same filename twice
    boolean existsByFileNameAndUserProfile(String fileName, UserProfile userProfile);

    // Statistics (Useful for Dashboard)
    long countByUserProfileId(UUID userId);

    // Advanced Lookups
    // Gets the most recently uploaded resume for a specific user
    Optional<Resume> findFirstByUserProfileIdOrderByUploadedAtDesc(UUID userId);

    // Cleanup
    void deleteAllByUserProfileId(UUID userId);
}