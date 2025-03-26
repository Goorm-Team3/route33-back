package io.goorm.route33.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 세션 엔티티 클래스
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "session")
public class Session {

    /**
     * 세션 ID
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    /**
     * access 토큰
     */
    @Column(name = "access_token")
    private String accessToken;

    /**
     * refresh 토큰
     */
    @Column(name = "refresh_token")
    private String refreshToken;

    /**
     * 생성 일시
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 만료 일시
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    /**
     * 회원 ID
     */
    @Column(name = "user_id")
    private Long userId;
}
