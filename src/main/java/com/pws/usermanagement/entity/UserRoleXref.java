package com.pws.usermanagement.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.pws.usermanagement.utility.AuditModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_role_xref")
public class UserRoleXref extends AuditModel {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("TRUE")
    private boolean isActive;

    @Column(name = "is_poster")
    private boolean isPoster;

    @Column(name = "is_applier")
    private boolean isApplier;

    public static class UserManagementException extends Exception {

        private static final long serialVersionUID = 1L;

        private String errorMessage;

        public UserManagementException(String errorMessage) {
            super(errorMessage);
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }


    }
}

