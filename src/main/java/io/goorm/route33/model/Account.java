package io.goorm.route33.model;

import io.goorm.route33.model.code.OnOffStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 계좌 엔티티 클래스
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "account")
public class Account {

    /**
     * 계좌 번호
     */
    @Id
    @Column(name = "account_number")
    private String accountNumber;

    /**
     * 잔액
     */
    @Column(name = "balance")
    private Integer balance;

    /**
     * 생성 일시
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 활성화 여부
     */
    @Column(name = "active_yn", columnDefinition = "TINYINT")
    private OnOffStatus activeYn;

    /**
     * 회원 ID
     */
    @Column(name = "user_id")
    private Long userId;

    public void depositBalance(int amount) {
        this.balance += amount;
    }
}
