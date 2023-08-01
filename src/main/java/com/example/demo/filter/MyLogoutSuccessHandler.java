package com.example.demo.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 扩展了 Spring Security 框架中的 SimpleUrlLogoutSuccessHandler，该类负责处理用户成功注销后的跳转行为
 */
@Service
@Slf4j
public class MyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Value("${dir.uri:test}")
    private String dir;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        super.handle(request, response, authentication);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response){

        String returnUri = "/";
        try {
            returnUri = URLEncoder.encode(String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort()), "utf-8");
        } catch (Exception e) {

        }
        if (!returnUri.contains("%3A8088") && returnUri.contains("%3A80")) {
//            returnUri = returnUri.replace("%3A80", "");
        }
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("/logout?return_uri=");
        stringBuilder.append(returnUri);
        stringBuilder.append("/home");
        return "/home";
    }
}
