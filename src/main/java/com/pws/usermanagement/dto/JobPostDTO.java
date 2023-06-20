package com.pws.usermanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobPostDTO {
    private String email;
    private String title;
    private String description;
    private double paymentAmount;
}
