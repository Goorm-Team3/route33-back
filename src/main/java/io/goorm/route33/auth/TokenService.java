
package io.goorm.route33.auth;

import io.goorm.route33.exception.CustomException;
import io.goorm.route33.model.User;
import io.goorm.route33.service.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class TokenService {
    private static final String USER_ID_CLAIM = "userId";

    private final SecretKey secretKey;
    private final long accessTokenExpirationMillis;
    private final long refreshTokenExpirationMillis;
    private final UserRepository userRepository;

    public TokenService(TokenProperty tokenProperty, UserRepository userRepository){


        byte[] keyBytes = Decoders.BASE64.decode(tokenProperty.secretKey());

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
//        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenProperty.secretKey()));
        this.accessTokenExpirationMillis = tokenProperty.accessTokenExpirationMillis();
        this.refreshTokenExpirationMillis = tokenProperty.refreshTokenExpirationMillis();
        this.userRepository = userRepository;
    }

    public String createAccessToken(Long userId){
        return Jwts.builder().claim(USER_ID_CLAIM,
                userId
        ).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMillis)).signWith(secretKey,
                Jwts.SIG.HS512).compact();
    }

    public String createRefreshToken(){
        return Jwts.builder().issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMillis)).signWith(secretKey,
                Jwts.SIG.HS512). compact();
    }

    public TokenPair createTokenPair(Long userId){
        String accessToken = createAccessToken(userId);
        String refreshToken = createRefreshToken();

        User user = userRepository.getByUserId(userId);
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenPair(accessToken,refreshToken);
    }

    public TokenPair refreshTokenPair(String refreshToken){
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(refreshToken);
        }catch (ExpiredJwtException e){
            throw new CustomException("만료된 Refresh Token 입니다.",HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            throw new CustomException("유효하지 않은 Refresh Token 입니다.", HttpStatus.UNAUTHORIZED);
        }
        User user = userRepository.getByRefreshToken(refreshToken);
        return createTokenPair(user.getUserId());
    }

    public Long extractUserId(String token){
        try{
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(USER_ID_CLAIM, Long.class);
        }catch (Exception e){
            throw new CustomException("유효하지 않은 Token입니다.", HttpStatus.UNAUTHORIZED);
        }
    }
}
