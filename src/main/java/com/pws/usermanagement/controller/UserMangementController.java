package com.pws.usermanagement.controller;

import com.pws.usermanagement.config.ApiSuccess;
import com.pws.usermanagement.dto.*;
import com.pws.usermanagement.entity.FeedBack;
import com.pws.usermanagement.entity.JobApplication;
import com.pws.usermanagement.entity.JobPost;
import com.pws.usermanagement.exception.UserManagementException;
import com.pws.usermanagement.exception.UserMgmtException;
import com.pws.usermanagement.service.UserManagementService;
import com.pws.usermanagement.utility.ApiError;
import com.pws.usermanagement.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserMangementController {

    private final UserManagementService service;

    @PostMapping("public/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDTO signUpDTO) {
        try {
            service.signUp(signUpDTO);
            return ResponseEntity.ok(new ApiSuccess(HttpStatus.OK, "User registered successfully"));
        } catch (UserManagementException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while signing up", ex.getMessage()));
        }
    }

    @PostMapping("public/role")
    public ResponseEntity<?> addRole(@RequestBody RoleDTO roleDTO) {
        try {
            service.addRole(roleDTO);
            return ResponseEntity.ok(new ApiSuccess(HttpStatus.OK, "Role added successfully"));
        } catch (UserManagementException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while adding role", ex.getMessage()));
        }
    }


    @PostMapping("/jobpost")
    public ResponseEntity<Object> createJobPost(@RequestBody JobPostDTO jobPostDTO) throws UserManagementException {
        try {
            service.createJobPost(jobPostDTO);
            return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, "Job post created successfully"));
        } catch (UserManagementException ex) {
            throw ex;
        }
    }


    @GetMapping("/all-jobposts")
    public ResponseEntity<?> getAllJobPosts(@RequestParam(defaultValue = "0") int pageNumber,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<JobPost> jobPosts = service.getAllJobPosts(pageNumber, pageSize);
            return ResponseEntity.ok(jobPosts);
        } catch (UserMgmtException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while retrieving job posts: " + ex.getMessage());
        }
    }

    @GetMapping("/jobpost/by-id")
    public ResponseEntity<?> getJobPostById(@RequestParam UUID id) {
        try {
            JobPost jobPost = service.getJobPostById(id);
            return ResponseEntity.ok(jobPost);
        } catch (UserManagementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(HttpStatus.NOT_FOUND, "Job post not found", ex.getMessage()));
        }
    }


    @PostMapping("/job-apply")
    public ResponseEntity<Object> applyForJob(@RequestParam UUID jobId, @RequestBody JobApplicationDTO jobApplicationDTO) {
        try {
            service.applyForJob(jobId, jobApplicationDTO);
            return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, "Job application submitted successfully"));
        } catch (UserManagementException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while applying for job", ex.getMessage()));
        }
    }


    @GetMapping("/applications")
    public ResponseEntity<?> getJobApplicationsByJobId(@RequestParam UUID jobId) {
        try {
            List<JobApplication> jobApplications = service.getJobApplicationsByJobId(jobId);
            return ResponseEntity.ok(jobApplications);
        } catch (UserManagementException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while retrieving job applications", ex.getMessage()));
        }
    }

    @PostMapping("/application/is-interested")
    public ResponseEntity<Object> updateJobApplicationInterest(@RequestParam UUID applicationId) {
        try {
            service.updateJobApplicationInterest(applicationId);
            return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, "Job application interest updated successfully"));
        } catch (UserManagementException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while updating job application interest", ex.getMessage()));
        }
    }

    @PostMapping("/applier-offer-chat")
    public ResponseEntity<String> applierOfferChat(@RequestBody ApplierChatDTO applierChatDTO) {
        try {
            service.applierOfferChat(applierChatDTO);
            return ResponseEntity.ok("Chat stored successfully");
        } catch (UserManagementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/poster-offer-chat")
    public ResponseEntity<String> posterOfferChat(@RequestBody PosterChatDTO posterChatDTO) {
        try {
            service.posterOfferChat(posterChatDTO);
            return ResponseEntity.ok("Chat stored successfully");
        } catch (UserManagementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add/job/status")
    public ResponseEntity<Object> addJobStatus(@RequestParam UUID applicationId, @RequestParam UUID jobId, @RequestParam boolean jobStatus, @RequestParam double amountPaidToAdmin) throws UserManagementException {
        service.addJobStatus(applicationId, jobId, jobStatus, amountPaidToAdmin);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @PostMapping("/add/feedback")
    public ResponseEntity<Object> addFeedBack(@RequestParam UUID applicationId, @RequestParam UUID jobId, @RequestParam FeedBack.Keyword feedBack, @RequestParam String workDescription) throws UserManagementException {
        service.addFeedBack(applicationId, jobId, feedBack, workDescription);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }
    @DeleteMapping("/delete/chat")
    public ResponseEntity<?> deleteChatById(@RequestParam UUID jobId, @RequestParam UUID applictionId, @RequestParam String posterEmail) throws UserManagementException {
        try {
            service.deleteChatById(jobId, applictionId, posterEmail);
            return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, "Chat deleted successfully"));
        } catch (UserManagementException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while deleting chat", ex.getMessage()));
        }
    }
    @PostMapping("/pay/to/applicant")
    public ResponseEntity<?> payToApplicant(@RequestParam UUID applicationId) throws UserManagementException {
        try {
            service.payToApplicant(applicationId);
            return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, "Amount paid to applicant successfully"));
        } catch (UserManagementException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while paying amount to applicant", ex.getMessage()));
        }
    }

}