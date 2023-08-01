package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GoogleOAuth2LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 返回登录页面的模板名称，例如"login.html"
    }

    @GetMapping("/login/oauth2/code/google")
    public String googleLoginCallback(@RequestParam("code") String code) {
        // 在这里处理授权码，并进行登录逻辑
        return "redirect:/123"; // 重定向到登录成功后的页面
    }
}
