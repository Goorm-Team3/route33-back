package io.goorm.route33.auth;

import io.goorm.route33.exception.CustomException;
import io.goorm.route33.service.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@RequiredArgsConstructor
@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Long userId = (Long) request.getAttribute("userId");


        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("존재하지 않는 사용자입니다.", HttpStatus.UNAUTHORIZED))
                .getUserId();
    }
}
