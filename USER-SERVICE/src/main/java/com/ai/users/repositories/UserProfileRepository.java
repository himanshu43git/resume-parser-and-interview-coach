package com.ai.users.repositories;

import com.ai.users.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    // Find user by the External Auth ID (Used for every login/sync)
    Optional<UserProfile> findByAuthUserId(String authUserId);

    // Check if user exists by External Auth ID
    boolean existsByAuthUserId(String authUserId);

    // Check if an email is already taken in the UserMS
    boolean existsByEmail(String email);

    // Find by email (Useful for administrative lookups)
    Optional<UserProfile> findByEmail(String email);

    // Delete a profile by the External Auth ID (Used for account deletion events)
    void deleteByAuthUserId(String authUserId);
}