package com.pws.usermanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDTO {

    private String name;
    private boolean isActive;

}
