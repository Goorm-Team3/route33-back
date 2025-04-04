package io.goorm.route33.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.goorm.route33.model.dto.UserLoginRequestDto;
import io.goorm.route33.model.dto.UserRegisterRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String username = "홍길동";
    private String loginId = "testuser1";
    private String password = "12341234";
    private String accessToken;
    private String refreshToken;

    @BeforeEach
    void setup() throws Exception {
        // 1. 회원가입
        UserRegisterRequestDto registerDto = new UserRegisterRequestDto(loginId, "테스트유저", password, password);
        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // 2. 로그인
        UserLoginRequestDto loginDto = new UserLoginRequestDto(loginId, password);
        MvcResult loginResult = mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        // Jackson으로 응답 파싱 (accessToken, refreshToken 추출)
        accessToken = objectMapper.readTree(loginResponse).get("accessToken").asText();
        refreshToken = objectMapper.readTree(loginResponse).get("refreshToken").asText();
    }

    @Test
    void RetrieveUserWithAccessToken() throws Exception {
        mockMvc.perform(get("/user/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    void ValidRefreshToken() throws Exception {
        String refreshRequest = "{ \"refreshToken\": \"" + refreshToken + "\" }";

        MvcResult result = mockMvc.perform(post("/token/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String newAccessToken = objectMapper.readTree(response).get("accessToken").asText();

        // 새 토큰으로 /user/me 접근
        mockMvc.perform(get("/user/me")
                        .header("Authorization", "Bearer " + newAccessToken))
                .andExpect(status().isOk());
    }

}
