package com.pws.usermanagement.repo;

import com.pws.usermanagement.entity.JobPost;
import com.pws.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JobPostRepository extends JpaRepository<JobPost, UUID> {
    @Query("select o from JobPost o where o.id= :jobPostId and o.user.email= :email")
    Optional<JobPost> findByPostIdAndEmail(UUID jobPostId, String email);

    @Query("select o.user from JobPost o where o.user.email= :posterEmail")
    Optional<User> findByPosterEmail(String posterEmail);
}
