package com.ai.users.services.implementation;

import com.ai.users.exceptions.InvalidUserDataException;
import com.ai.users.exceptions.UserAlreadyExistsException;
import com.ai.users.exceptions.UserDoesNotExistException;
import com.ai.users.io.request.UserSyncRequest;
import com.ai.users.io.response.UserProfileResponse;
import com.ai.users.models.UserProfile;
import com.ai.users.repositories.UserProfileRepository;
import com.ai.users.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userRepository;

    @Override
    @Transactional
    public UserProfileResponse syncUser(UserSyncRequest request) {
        log.info("Received sync request for email: {} and authId: {}", request.getEmail(), request.getAuthUserId());

        if (userRepository.existsByAuthUserId(request.getAuthUserId())) {
            log.error("Sync failed: User with auth ID {} already exists.", request.getAuthUserId());
            throw new UserAlreadyExistsException("User with auth ID " + request.getAuthUserId() + " already exists.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Sync conflict: Email {} is already registered to another auth ID.", request.getEmail());
            throw new UserAlreadyExistsException("Email already exists in the system.");
        }

        UserProfile newUser = UserProfile.builder()
                .authUserId(request.getAuthUserId())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .build();

        newUser = userRepository.save(newUser);
        log.info("Successfully created new user profile with internal ID: {}", newUser.getId());

        return mapToResponse(newUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserById(UUID id) {
        log.debug("Fetching user profile for internal ID: {}", id);

        if (id == null) {
            log.warn("getUserById called with null ID");
            throw new InvalidUserDataException("User ID cannot be null.");
        }

        UserProfile user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User retrieval failed: ID {} does not exist.", id);
            return new UserDoesNotExistException("User with ID " + id + " does not exist.");
        });

        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserByAuthId(String authUserId) {
        log.debug("Fetching user profile for Auth ID: {}", authUserId);

        if (authUserId == null || authUserId.isEmpty()) {
            log.warn("getUserByAuthId called with empty/null authUserId");
            throw new InvalidUserDataException("Auth User ID cannot be null or empty.");
        }

        UserProfile user = userRepository.findByAuthUserId(authUserId).orElseThrow(() -> {
            log.error("User retrieval failed: Auth ID {} not found.", authUserId);
            return new UserDoesNotExistException("User with Auth ID " + authUserId + " does not exist.");
        });

        return mapToResponse(user);
    }

    /**
     * Internal helper to map Entity to Response DTO.
     */
    private UserProfileResponse mapToResponse(UserProfile user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}