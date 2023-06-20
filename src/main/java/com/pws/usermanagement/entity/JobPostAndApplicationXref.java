package com.pws.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "job_post_application_xref")
public class JobPostAndApplicationXref {

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
    @JoinColumn(name = "job_id",nullable = false)
    private JobPost jobPost;

    @ManyToOne
    @JoinColumn(name = "application_id",nullable = false)
    private JobApplication application;

    @Column(name = "amount_paid_to_admin")
    private double amountPaidToAdmin;

    @ColumnDefault("false")
    private boolean jobStatus;

}
