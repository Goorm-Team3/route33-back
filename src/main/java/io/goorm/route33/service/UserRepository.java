package io.goorm.route33.service;

import io.goorm.route33.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByRefreshToken(String refreshToken);

    default User getByRefreshToken(String refreshToken){
        return findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException());
        //TODO : Exception handle 필요
    }
}
