package com.pws.usermanagement.utility;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    @JsonIgnore
    public Date createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    @JsonIgnore
    @CreatedBy
    public String createdBy;

    @Column(name = "updated_at", nullable = false, updatable = true)
    @JsonIgnore
    @LastModifiedDate
    public Date updatedAt;

    @Column(name = "updated_by", nullable = false, updatable = true)
    @JsonIgnore
    @LastModifiedBy
    public String updatedBy;




}

