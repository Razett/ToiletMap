package com.razett.toiletmap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 기본(Root) Entity
 *
 * @since 2025-05-25 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity {

    /** 생성일 */
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime created;

    /** 수정일 */
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updated;

}
