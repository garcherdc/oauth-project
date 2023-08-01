package com.example.demo;


import com.example.demo.entity.OperationLogDTO;
import com.example.demo.entity.UserInfo;
import com.example.demo.filter.*;
import com.example.demo.manager.HttpClientManager;
import com.example.demo.manager.kafka.SystemConfig;
import com.example.demo.service.OperationLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.saml.*;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import util.Constants;

import javax.annotation.Resource;


/**
 * 1. AppMain里排除OAuth2AutoConfiguration.class, SecurityAutoConfiguration.class，防止国内的情况也自动注册了security登录
 * 2. OAuth2ClientConfiguration需要在OAuth2RestOperationsConfiguration初始化之前
 * @EnableRedisHttpSession 注解会告诉 Spring Boot 将 Session 存储在 Redis 中
 */
@EnableWebSecurity
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 7200, redisNamespace = "Wt")
@ImportAutoConfiguration({OAuth2AutoConfiguration.class, SecurityAutoConfiguration.class})

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private HttpClientManager httpClientManager;
    @Resource
    private OperationLogService operationLogService;

    @Value("${ignore.login.uri}")
    private String ignoreLoginUri = "/ok,/ok.htm,/favicon.ico,/login**,/logout**,*.html,*.css,*.js,/test/**";

    @Autowired
    SecurityContextLogoutHandler logoutHandler;
    @Autowired
    SystemConfig systemConfig;
    @Autowired
    MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Bean
    public SecurityContextLogoutHandler logoutHandler() {
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        //handler.setInvalidateHttpSession(true);
        handler.setClearAuthentication(true);
        return handler;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsService();
//    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        String[] ignoreLoginUriArr = ignoreLoginUri.split(",");
//        //授权认证
//        http
//                //关闭csrf
//                .csrf().disable()
//                // 开启跨域以便前端调用接口
//                .cors().and()
//                .authorizeRequests()
//                ///googleLogin不需要被认证，可以被放行
//                .antMatchers(ignoreLoginUriArr).permitAll()
//                //所有请求必须认证，必须登录后才能访问
//                .anyRequest()
//                .authenticated().and()
//        //使用自定义provider
//                //TokenAuthenticationFilter放到UsernamePasswordAuthenticationFilter的前面，这样做就是为了除了登录的时候去查询数据库外，其他时候都用token进行认证。
////                .authenticationProvider(jwtAuthenticationProvider())
//                .oauth2Login();
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] ignoreLoginUriArr = ignoreLoginUri.split(",");


        DomainSwitchFilter domainSwitchFilter = new DomainSwitchFilter();
        UserInfoFilter userInfoFilter = new UserInfoFilter();
        userInfoFilter.setSystemConfig(systemConfig);
        userInfoFilter.setOperationLogService(operationLogService);
        http

                .addFilterBefore(domainSwitchFilter, SecurityContextPersistenceFilter.class)
                .addFilterAfter(userInfoFilter, LogoutFilter.class)
                // 默认所有请求都需要登录授权认证
                .antMatcher("/**")
                .authorizeRequests()
                // 允许这些URL无需认证
                .antMatchers(ignoreLoginUriArr)
                .permitAll()
                .anyRequest()
                .authenticated()

                // 登出action
                .and()
                .oauth2Login()
                .and()
                .logout().logoutUrl("/logout").permitAll()
                .addLogoutHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    UserInfo userInfo = (UserInfo) httpServletRequest.getSession().getAttribute(Constants.SESSION_USER_INFO);
                    if(userInfo!=null) {
                        //操作日志
                        OperationLogDTO operationLogDTO = new OperationLogDTO();
                        operationLogDTO.setUsername(userInfo.getUserName());
                        operationLogDTO.setEmail(userInfo.getEmail());
                        operationLogDTO.setOperation(1);
                        operationLogDTO.setOperationTime(System.currentTimeMillis());
                        operationLogService.addOperationLog(operationLogDTO);
                    }
                    logger.info("LogoutHandler request.getScheme() :{},request.getHeader: {}", httpServletRequest.getScheme(),
                            httpServletRequest.getHeader("referer"));
                })
                .addLogoutHandler(logoutHandler).deleteCookies(Constants.COOKIE_SESSION)
                .logoutSuccessHandler(myLogoutSuccessHandler)

                // 安全措施
                .and()
                .headers().frameOptions().disable().and().csrf().disable();
    }

}