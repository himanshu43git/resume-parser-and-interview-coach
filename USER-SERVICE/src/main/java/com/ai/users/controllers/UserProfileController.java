package com.ai.users.controllers;

import com.ai.users.io.request.UserSyncRequest;
import com.ai.users.io.response.UserProfileResponse;
import com.ai.users.services.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
//@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userService;

    public UserProfileController(UserProfileService userService) {
        this.userService = userService;
    }

    @PostMapping("/sync")
    public ResponseEntity<UserProfileResponse> syncUser(@Valid @RequestBody UserSyncRequest request) {
        return new ResponseEntity<>(userService.syncUser(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/auth/{authUserId}")
    public ResponseEntity<UserProfileResponse> getUserByAuthId(@PathVariable String authUserId) {
        return ResponseEntity.ok(userService.getUserByAuthId(authUserId));
    }
}