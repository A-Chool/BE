package com.RoutineGongJakSo.BE.model;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter // 변수가 전부 piravte 임으로 GET이 필요하다.
@MappedSuperclass // 상속했을 때, 컬럼으로 인식하게 합니다.컬럼=열
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {

    @CreatedDate // 생성일자임을 나타냅니다.
    private String createdAt;

    @LastModifiedDate // 마지막 수정일자임을 나타냅니다.
    private String modifiedAt;


    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.modifiedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @PreUpdate
    public void onPreUpdate() {
        this.modifiedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}