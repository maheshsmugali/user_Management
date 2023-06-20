package com.pws.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "feed_back")
public class FeedBack {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "feed_back_by_poster",nullable = false)
    private Keyword workFeedBack;

    @Column(name = "work_description",nullable = true)
    private String workDescription;
    public enum Keyword {
        satisfied, notsatisfied
    }

}
