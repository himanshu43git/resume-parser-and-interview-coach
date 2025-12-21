package com.ai.users.services;

import com.ai.users.io.request.UserSyncRequest;
import com.ai.users.io.response.UserProfileResponse;
import java.util.UUID;

public interface UserProfileService {

    // Syncs user data from Auth Service
    UserProfileResponse syncUser(UserSyncRequest request);

    // Retrieves profile details for the frontend
    UserProfileResponse getUserById(UUID id);

    // Fetches profile by Auth ID (Internal use)
    UserProfileResponse getUserByAuthId(String authUserId);

}