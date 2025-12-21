package com.ai.users.controllers;

import com.ai.users.io.request.ResumeSyncRequest;
import com.ai.users.io.response.ResumeResponse;
import com.ai.users.services.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resumes")
//@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // Called by Python FastAPI Service
    @PostMapping("/sync")
    public ResponseEntity<ResumeResponse> syncResume(@Valid @RequestBody ResumeSyncRequest request) {
        return ResponseEntity.ok(resumeService.saveParsedResume(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResumeResponse>> getResumesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(resumeService.getAllResumesByUserId(userId));
    }

    @DeleteMapping("/{resumeId}")
    public ResponseEntity<Void> deleteResume(@PathVariable UUID resumeId) {
        resumeService.deleteResume(resumeId);
        return ResponseEntity.noContent().build();
    }
}