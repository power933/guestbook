package com.kcs.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//엔티티로 사용은 가능하지만 테이블을 생성하진 않음
@MappedSuperclass
//jpa를 감시하고 있다가 데이터를 수정
@EntityListeners(value={AuditingEntityListener.class})
@Getter
public abstract class BaseEntity {
    //데이터 생성 날짜 설정
    @CreatedDate
    @Column(name="regdate",updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name="lastdate")
    private LocalDateTime modDate;
}
