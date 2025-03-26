package io.goorm.route33.model;

import io.goorm.route33.model.code.OnOffStatus;
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
 * 회원 엔티티 클래스
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class User {

    /**
     * 회원 ID
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /**
     * 사용자 이름
     */
    @Column(name = "username")
    private String username;

    /**
     * 로그인 아이디
     */
    @Column(name = "login_id")
    private String loginId;

    /**
     * 로그인 비밀번호
     */
    @Column(name = "password")
    private String password;

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
}
