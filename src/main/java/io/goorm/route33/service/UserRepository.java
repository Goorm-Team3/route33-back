package io.goorm.route33.service;

import io.goorm.route33.exception.CustomException;
import io.goorm.route33.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);



    Optional<User> findByLoginId(String loginId);

    Optional<User> findByRefreshToken(String refreshToken);

    default User getByRefreshToken(String refreshToken){
        return findByRefreshToken(refreshToken).orElseThrow(() -> new CustomException("존재하지 않는 리프레시 토큰입니다.",HttpStatus.UNAUTHORIZED));
        //TODO : Exception handle 필요
    }
    default User getByUserId(Long id){
        return findById(id).orElseThrow(() -> new CustomException("사용자를 찾지 못했습니다.", HttpStatus.NOT_FOUND));
    }
}
