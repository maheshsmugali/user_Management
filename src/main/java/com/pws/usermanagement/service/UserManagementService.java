package com.pws.usermanagement.service;

import com.pws.usermanagement.dto.*;
import com.pws.usermanagement.entity.FeedBack;
import com.pws.usermanagement.entity.JobApplication;
import com.pws.usermanagement.entity.JobPost;
import com.pws.usermanagement.exception.UserManagementException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface UserManagementService {
    void signUp(SignUpDTO signUpDTO) throws UserManagementException;

    void addRole(@RequestBody RoleDTO roleDTO) throws UserManagementException;

    void createJobPost(JobPostDTO jobPostDTO) throws UserManagementException;

    Page<JobPost> getAllJobPosts(int pageNumber, int pageSize);

    JobPost getJobPostById(UUID id) throws UserManagementException;

    void applyForJob( UUID jobId, JobApplicationDTO jobApplicationDTO) throws UserManagementException;

    List<JobApplication> getJobApplicationsByJobId(UUID jobId) throws UserManagementException;

    void updateJobApplicationInterest(UUID applicationId) throws UserManagementException;

    void applierOfferChat(ApplierChatDTO applierChatDTO) throws UserManagementException;

    void posterOfferChat(PosterChatDTO posterChatDTO) throws UserManagementException;

    void addJobStatus(UUID applicationId,UUID jobId,boolean jobStatus,double amountPaidToAdmin)throws UserManagementException;

    void addFeedBack(UUID applicationId, UUID jobId, FeedBack.Keyword feedBack, String workDescription) throws UserManagementException;

    void payToApplicant(UUID applicationId) throws UserManagementException;

    void deleteChatById(UUID jobId, UUID applictionId, String posterEmail) throws UserManagementException;
}
