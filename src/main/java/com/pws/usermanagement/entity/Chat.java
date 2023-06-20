package com.pws.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pws.usermanagement.utility.AuditModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")
public class Chat extends AuditModel {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Column(name = "poster_id",nullable = false)
    private UUID posterId;

    @Column(name = "job_post_id",nullable = false)
    private UUID jobPostId;

    @Column(name = "applier_id",nullable = false)
    private UUID applierId;

    @Column(name = "job_poster_chat")
    private String jobPosterChat;

    @Column(name = "job_applier_chat")
    private String jobApplierChat;



}