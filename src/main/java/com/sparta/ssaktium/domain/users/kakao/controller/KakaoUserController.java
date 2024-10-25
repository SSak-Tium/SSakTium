package com.sparta.ssaktium.domain.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.ssaktium.config.ApiResponse;
import com.sparta.ssaktium.config.JwtUtil;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.kakao.service.KakaoUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class KakaoUserController {

    private final KakaoUserService kakaoUserService;

    @GetMapping("/v1/ssaktium/home")
    public String loginPage() {
        return "home";
    }

    @GetMapping("/v1/signin-kakao")
    public String kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        String token  = kakaoUserService.kakaoLogin(code, response);
        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);
        return "main";
    }
}