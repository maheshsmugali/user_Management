package com.pws.usermanagement.repo;

import com.pws.usermanagement.entity.JobApplication;
import com.pws.usermanagement.entity.JobPostAndApplicationXref;
import com.pws.usermanagement.entity.Role;
import com.pws.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobPostAndApplicationXrefRepository extends JpaRepository<JobPostAndApplicationXref, UUID> {
    @Query("select o.application from JobPostAndApplicationXref o where o.jobPost.id=:jobId")
    List<JobApplication> findByJobPostId(UUID jobId);

    @Query("select o from JobPostAndApplicationXref o where  o.application.id=:applicationId and o.jobPost.id=:jobId")
    Optional<JobPostAndApplicationXref> findByjobIdApplicationId(UUID applicationId, UUID jobId);
    @Query("select o.application from JobPostAndApplicationXref o where  o.application.applier=:user and o.jobPost.id=:id")
    Optional<JobApplication> findByUserJobPostId(User user, UUID id);
    @Query("select o from JobPostAndApplicationXref o where  o.application.id=:applicationId")
    Optional<JobPostAndApplicationXref> findByApplicationId(UUID applicationId);

}
