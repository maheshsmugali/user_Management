package com.pws.usermanagement.service;

import com.pws.usermanagement.dto.*;
import com.pws.usermanagement.entity.*;
import com.pws.usermanagement.exception.UserManagementException;
import com.pws.usermanagement.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ChatRepository chatRepository;

    private final JobPostRepository jobPostRepository;

    private final FeedBackRepository feedBackRepository;

    private final UserRoleXrefRepository userRoleXrefRepository;

    private final JobApplicationRepository jobApplicationRepository;

    private final PaidToApplicantRepository paidToApplicantRepository;

    private final JobPostAndApplicationXrefRepository jobPostAndApplicationXrefRepository;

    @Override
    public void signUp(SignUpDTO signUpDTO) throws UserManagementException {

        if (!isStrongPassword(signUpDTO.getPassword())) {
            throw new UserManagementException("Please enter strong password, at least one uppercase letter, one lowercase letter, one digit, and one special character needed");
        }
        Optional<User> optionalUser = userRepository.findByEmail(signUpDTO.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserManagementException("User Already registered please sign in");
        } else {
            User user = new User();
            user.setDateOfBirth(signUpDTO.getDateOfBirth());
            user.setFirstName(signUpDTO.getFirstName());
            user.setLastName(signUpDTO.getLastName());
            user.setEmail(signUpDTO.getEmail().toLowerCase());
            user.setPhoneNumber(signUpDTO.getPhoneNumber());
            user.setGender(signUpDTO.getGender());
            user.setActive(true);
            String userId = user.getEmail();

            PasswordEncoder encoder = new BCryptPasswordEncoder(8);
            user.setPassword(encoder.encode(signUpDTO.getPassword()));

            UserRoleXref userRoleXref = new UserRoleXref();
            userRoleXref.setUser(user);
            Optional<Role> roleOptional = roleRepository.findRoleByName(signUpDTO.getRole());
            if (!roleOptional.isPresent()) {
                throw new UserManagementException("Role not found: " + signUpDTO.getRole());
            } else {
                userRoleXref.setRole(roleOptional.get());
                userRoleXref.setActive(true);
                if (signUpDTO.getRole().equalsIgnoreCase("poster")) {
                    userRoleXref.setPoster(true);
                } else {
                    userRoleXref.setApplier(true);
                }
                userRepository.save(user);
                userRoleXrefRepository.save(userRoleXref);
            }
        }
    }

    @Override
    public void addRole(RoleDTO roleDTO) throws UserManagementException {
        String roleName = roleDTO.getName().toLowerCase();
        if (!roleRepository.findRoleByName(roleName).isPresent()) {
            Role role = new Role();
            role.setName(roleName);
            role.setActive(roleDTO.isActive());
            roleRepository.save(role);
        } else {
            throw new UserManagementException("Role with name " + roleDTO.getName() + " already exists");
        }
    }

    @Override
    public void createJobPost(JobPostDTO jobPostDTO) throws UserManagementException {
        validateJobPostData(jobPostDTO);

        Optional<User> optionalUser = userRepository.findByEmail(jobPostDTO.getEmail().toLowerCase());

        if (!optionalUser.isPresent()) {
            throw new UserManagementException("User not found with email: " + jobPostDTO.getEmail());
        }

        Role userRole = userRoleXrefRepository.findRoleByUserId(optionalUser.get().getId());

        if (!userRole.getName().equalsIgnoreCase("poster")) {
            UserRoleXref userRoleXref = UserRoleXref.builder()
                    .isPoster(true)
                    .isApplier(false)
                    .build();
            userRoleXrefRepository.save(userRoleXref);

            JobPost jobPost = new JobPost();
            jobPost.setTitle(jobPostDTO.getTitle());
            jobPost.setDescription(jobPostDTO.getDescription());
            jobPost.setPaymentAmount(jobPostDTO.getPaymentAmount());
            jobPost.setUser(optionalUser.get());
            jobPostRepository.save(jobPost);

        } else {
            JobPost jobPost = new JobPost();
            jobPost.setUser(optionalUser.get());
            jobPost.setTitle(jobPostDTO.getTitle());
            jobPost.setDescription(jobPostDTO.getDescription());
            jobPost.setPaymentAmount(jobPostDTO.getPaymentAmount());

            jobPostRepository.save(jobPost);
        }
    }

    @Override
    public Page<JobPost> getAllJobPosts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return jobPostRepository.findAll(pageable);
    }

    @Override
    public JobPost getJobPostById(UUID id) throws UserManagementException {
        Optional<JobPost> optionalJobPost = jobPostRepository.findById(id);
        if (optionalJobPost.isPresent()) {
            return optionalJobPost.get();
        } else {
            throw new UserManagementException("Job Post not found with ID: " + id);
        }
    }

    @Override
    public void applyForJob(UUID jobId, JobApplicationDTO jobApplicationDTO) throws UserManagementException {
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new UserManagementException("Job post not found"));

        User user = userRepository.findByEmail(jobApplicationDTO.getEmail())
                .orElseThrow(() -> new UserManagementException("User not found"));
        if (jobPost.getUser().getId() == user.getId()) {
            throw new UserManagementException("Job poster cannot apply for the same job posted by him");
        }
        Optional<JobApplication> optionalJobApplication = jobApplicationRepository.findByEmail(user.getEmail());
        if (optionalJobApplication.isPresent()) {
            Optional<JobApplication> application = jobPostAndApplicationXrefRepository.findByUserJobPostId(user, jobPost.getId());
            if (application.isPresent()) {
                throw new UserManagementException("You have already applied for this job");
            }
        }
        JobApplication jobApplication = JobApplication.builder()
                .applier(user)
                .education(jobApplicationDTO.getEducation())
                .experience(jobApplicationDTO.getExperience())
                .build();

        jobApplicationRepository.save(jobApplication);
        JobPostAndApplicationXref xref = JobPostAndApplicationXref.builder()
                .jobPost(jobPost)
                .application(jobApplication)
                .build();
        jobPostAndApplicationXrefRepository.save(xref);
    }

    @Override
    public List<JobApplication> getJobApplicationsByJobId(UUID jobId) throws UserManagementException {
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new UserManagementException("Job post not found"));

        return jobPostAndApplicationXrefRepository.findByJobPostId(jobId);
    }

    @Override
    public void updateJobApplicationInterest(UUID applicationId) throws UserManagementException {

        Optional<JobApplication> application = jobApplicationRepository.findById(applicationId);
        if (!application.isPresent()) {
            throw new UserManagementException("Job Appliction not found");
        }
        JobApplication jobApplication = application.get();
        jobApplication.setIntrested(true);
        jobApplicationRepository.save(jobApplication);
    }


    @Override
    public void applierOfferChat(ApplierChatDTO applierChatDTO) throws UserManagementException {
        Optional<JobApplication> jobApplication = jobApplicationRepository.findByEmail(applierChatDTO.getApplierEmail());
        if (!jobApplication.isPresent()) {
            throw new UserManagementException("Job application not found with the name " + applierChatDTO.getApplierEmail());
        }
        if (!(jobApplication.get().isIntrested() == true)) {
            throw new UserManagementException("Your job application is not selected");
        }
        Optional<User> applier = userRepository.findByEmail(applierChatDTO.getApplierEmail());

        Optional<User> poster = jobPostRepository.findByPosterEmail(applierChatDTO.getPosterEmail());
        if (!poster.isPresent()) {
            throw new UserManagementException("Provide the correct poster");
        }

        Chat acceptedOffer = Chat.builder()
                .jobApplierChat(applierChatDTO.getJobApplierChat())
                .jobPostId(applierChatDTO.getJobPostId())
                .applierId(applier.get().getId())
                .posterId(poster.get().getId())
                .build();
        chatRepository.save(acceptedOffer);
    }

    @Override
    public void posterOfferChat(PosterChatDTO posterChatDTO) throws UserManagementException {

        Optional<JobPost> jobPost = jobPostRepository.findByPostIdAndEmail(posterChatDTO.getJobPostId(), posterChatDTO.getPosterEmail());
        if (!jobPost.isPresent()) {
            throw new UserManagementException("Job post not found with poster " + posterChatDTO.getPosterEmail());
        }

        Optional<User> applier = userRepository.findByEmail(posterChatDTO.getApplierEmail());
        if (!applier.isPresent()) {
            throw new UserManagementException("please provide correct applier ");
        }

        Chat acceptedOffer = Chat.builder()
                .jobPosterChat(posterChatDTO.getJobPosterChat())
                .jobPostId(jobPost.get().getId())
                .applierId(applier.get().getId())
                .posterId(posterChatDTO.getJobPostId())
                .build();

        chatRepository.save(acceptedOffer);
    }

    @Override
    public void addJobStatus(UUID applicationId, UUID jobId, boolean jobStatus, double amountPaidToAdmin) throws UserManagementException {
        Optional<JobPostAndApplicationXref> optionalJobApplication = jobPostAndApplicationXrefRepository.findByjobIdApplicationId(applicationId, jobId);
        if (!optionalJobApplication.isPresent()) {
            throw new UserManagementException("Invalid applicationId/jobId");
        }

        JobPostAndApplicationXref jobApplication = optionalJobApplication.get();
        jobApplication.setAmountPaidToAdmin(amountPaidToAdmin);
        jobApplication.setJobStatus(jobStatus);
        jobPostAndApplicationXrefRepository.save(jobApplication);
    }

    @Override
    public void addFeedBack(UUID applicationId, UUID jobId, FeedBack.Keyword feedBack, String workDescription) throws UserManagementException {
        Optional<JobPostAndApplicationXref> optionalJobApplication = jobPostAndApplicationXrefRepository.findByjobIdApplicationId(applicationId, jobId);
        if (!optionalJobApplication.isPresent()) {
            throw new UserManagementException("Invalid applicationId or jobId");
        }
        if (!optionalJobApplication.get().isJobStatus() == true) {
            throw new UserManagementException("job appliction not selected");
        }
        feedBackRepository.save(FeedBack.builder()
                .application(optionalJobApplication.get().getApplication())
                .jobPost(optionalJobApplication.get().getJobPost())
                .workFeedBack(feedBack)
                .workDescription(workDescription)
                .build());
    }

    @Override
    public void deleteChatById(UUID jobId, UUID applictionId, String posterEmail) throws UserManagementException {

        Optional<User> optionalPoster = userRepository.findByEmail(posterEmail);
        if (!optionalPoster.isPresent()) {
            throw new UserManagementException("Provide correct Poster email");
        }
        Optional<User> optionalApplier = jobApplicationRepository.findByAppId(applictionId);

        Optional<JobPostAndApplicationXref> optionalJobPostAndApplicationXref = jobPostAndApplicationXrefRepository.findByjobIdApplicationId(applictionId, jobId);
        if (!optionalJobPostAndApplicationXref.isPresent()) {
            throw new UserManagementException("JobPostAndApplicationXref not found for provided data");
        }
        if (optionalJobPostAndApplicationXref.get().isJobStatus()) {
            throw new UserManagementException("JobStatus is true. Cannot delete accepted job offer chats.");
        }

        List<Chat> chats = chatRepository.findByJobPostIdAndApplierIdAndPosterId(jobId, optionalApplier.get().getId(), optionalPoster.get().getId());
        if (chats.isEmpty()) {
            throw new UserManagementException("No chats found for the provided data");
        }

        chatRepository.deleteAll(chats);
    }


    @Override
    public void payToApplicant(UUID applicationId) throws UserManagementException {

        Optional<JobPostAndApplicationXref> optionalJobPostAndApplicationXref = jobPostAndApplicationXrefRepository.findByApplicationId(applicationId);
        if (!optionalJobPostAndApplicationXref.isPresent()) {
            throw new UserManagementException("Job application not found with this id " + applicationId);
        }
        boolean jobStatus = optionalJobPostAndApplicationXref.get().isJobStatus();
        if (jobStatus == false) {
            throw new UserManagementException("Your application is not selected");
        }
        double amount = optionalJobPostAndApplicationXref.get().getAmountPaidToAdmin();
        if (amount <= 0) {
            throw new UserManagementException("amount cannot be zero or negative");
        }

        Optional<FeedBack> optionalFeedBack = feedBackRepository.findByApplicationId(applicationId);
        if (!optionalFeedBack.isPresent()) {
            throw new UserManagementException("feedback by poster not found fou this application/applicant " + applicationId);
        }
        double finalAmountToBePaid = 0;
        if (optionalFeedBack.get().getWorkFeedBack().equals(FeedBack.Keyword.satisfied)) {
            finalAmountToBePaid = amount;
        }
        if (optionalFeedBack.get().getWorkFeedBack().equals(FeedBack.Keyword.notsatisfied)) {
            finalAmountToBePaid = amount / 2;
        }
        paidToApplicantRepository.save(PaidToApplicant.builder()
                .application(optionalJobPostAndApplicationXref.get().getApplication())
                .amountPaid(finalAmountToBePaid)
                .build());

    }


    private boolean isStrongPassword(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (isSpecialChar(ch)) {
                hasSpecialChar = true;
            }
        }

        return password.length() >= 8 && hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private void validateJobPostData(JobPostDTO jobPostDTO) throws UserManagementException {
        if (jobPostDTO.getTitle() == null || jobPostDTO.getTitle().isEmpty()) {
            throw new UserManagementException("Title is required");
        }
        if (jobPostDTO.getTitle().length() > 30) {
            throw new UserManagementException("Title should not exceed 30 characters");
        }

        if (jobPostDTO.getDescription() == null || jobPostDTO.getDescription().isEmpty()) {
            throw new UserManagementException("Description is required");
        }
        if (jobPostDTO.getDescription().length() > 250) {
            throw new UserManagementException("Description should not exceed 250 characters");
        }

        if (jobPostDTO.getPaymentAmount() <= 0) {
            throw new UserManagementException("Payment amount should be greater than zero");
        }
    }

    private boolean isSpecialChar(char ch) {
        String specialChars = "!@#$%^&*()_-+=[{]};:<>|./?";
        return specialChars.contains(Character.toString(ch));
    }

}


