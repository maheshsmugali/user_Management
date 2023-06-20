package com.pws.usermanagement.repo;

import com.pws.usermanagement.entity.JobApplication;
import com.pws.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {
    @Query("select o from JobApplication o where o.applier.email= :email")
    Optional<JobApplication> findByEmail(String email);
    @Query("select o.applier from JobApplication o where o.id= :applictionId")
    Optional<User> findByAppId(UUID applictionId);
}
