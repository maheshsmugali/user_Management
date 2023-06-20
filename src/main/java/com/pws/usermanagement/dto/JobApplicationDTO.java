package com.pws.usermanagement.dto;

import com.pws.usermanagement.entity.User;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobApplicationDTO {

    private String email;

    private String education;

    private double experience;

}
