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
@Table(name = "job_application")
public class JobApplication extends AuditModel {
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
    @JoinColumn(name = "applier_id", nullable = false)
    private User applier;

    @Column(name = "education", nullable = false)
    private String education;

    @Column(name = "experience")
    private double experience;

    @ColumnDefault("FALSE")
    private boolean isIntrested;


}
