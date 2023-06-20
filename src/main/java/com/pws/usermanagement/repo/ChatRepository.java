package com.pws.usermanagement.repo;

import com.pws.usermanagement.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    @Query("select o from Chat o where o.jobPostId= :jobPostId and o.applierId= :applierId and o.posterId= :posterId")
    List<Chat> findByJobPostIdAndApplierIdAndPosterId(UUID jobPostId, UUID applierId, UUID posterId);

}
