package com.sparta.ssaktium.domain.users.customOauth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.ssaktium.config.JwtUtil;
import com.sparta.ssaktium.domain.common.dto.AuthUser;
import com.sparta.ssaktium.domain.users.customOauth.service.CustomOauthService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CustomOauthController {

    @Value("${app.domainUri}")
    private String domainUri;

    private JwtUtil jwtUtil;


    private final CustomOauthService customOauthService;

    @GetMapping("/ssaktium/home")
    public String home(Model model) {
        model.addAttribute("domainUri", domainUri);
        return "home";
    }

    @GetMapping("/ssaktium/main")
    public String main() {
        return "main";
    }


    @GetMapping("/ssaktium/signup")
    public String signupPage() {
        return "signup"; // signup.html 페이지를 반환
    }

    // 소셜로그인
    @GetMapping("/ssaktium/signin/{provider}")
    public String socialLogin(
            @PathVariable("provider") String provider,
            @RequestParam("code") String code, HttpServletResponse response) throws JsonProcessingException {

        String token = customOauthService.socialLogin(provider, code, response);
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/ssaktium/main";
    }
}
