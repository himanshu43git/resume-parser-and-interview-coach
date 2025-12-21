package com.ai.users.io.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSyncRequest {
    private String authUserId;
    private String email;
    private String fullName;
}