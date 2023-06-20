package com.pws.usermanagement.repo;

import com.pws.usermanagement.entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface FeedBackRepository extends JpaRepository<FeedBack, UUID> {
    @Query("select o from FeedBack o where o.application.id= :applicationId")
    Optional<FeedBack> findByApplicationId(UUID applicationId);
}
