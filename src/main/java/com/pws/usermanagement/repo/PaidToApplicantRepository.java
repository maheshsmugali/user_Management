package com.pws.usermanagement.repo;

import com.pws.usermanagement.entity.PaidToApplicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaidToApplicantRepository extends JpaRepository<PaidToApplicant, UUID> {
}
