package com.pws.usermanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PosterChatDTO {

    private String posterEmail;

    private String applierEmail;

    private UUID jobPostId;

    private String jobPosterChat;

    private boolean posterAgreed;

    private double amount;
}
