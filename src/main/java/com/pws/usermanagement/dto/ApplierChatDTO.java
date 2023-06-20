package com.pws.usermanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ApplierChatDTO {

    private String posterEmail;

    private String applierEmail;

    private UUID jobPostId;

    private String jobApplierChat;

    private boolean applierAgreed;

    private double amount;
}
