package com.example.demo.filter;

import com.example.demo.entity.OperationLogDTO;
import com.example.demo.entity.UserInfo;
import com.example.demo.manager.kafka.SystemConfig;
import com.example.demo.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import util.CommonUtil;
import util.Constants;
import util.SessionContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class UserInfoFilter implements Filter {
    private SystemConfig systemConfig;
    private OperationLogService operationLogService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if(authentication==null){
            filterChain.doFilter(req,resp);
            return;
        }
        UserInfo userInfo = getAndCacheUser(req, authentication);

        /**
         * 如果cookie里的用户不住列表内，则不设置当前选择
         */
        //获取请求中的语言参数
        String partnerCodeSelect = req.getHeader("p-code");
        if(StringUtils.isBlank(partnerCodeSelect)) {
            partnerCodeSelect = CommonUtil.getCookie(req, Constants.COOKIE_PARTNER_CODE);
        }
        if (StringUtils.isNotBlank(partnerCodeSelect)
                && userInfo.getAccessPartners() != null
                && userInfo.getAccessPartners().contains(partnerCodeSelect)) {
            userInfo.setSelectPartner(partnerCodeSelect);
        }
        SessionContext.save(userInfo);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            SessionContext.clear();
        }

    }
/**
 * {sub=108686405279999124364, name=w峂, given_name=峂, family_name=w, picture=, email=, email_verified=true, locale=zh-CN}

 */
    private UserInfo getAndCacheUser(HttpServletRequest req, Authentication authentication) {
        //redis 获取用户信息
        UserInfo userInfo = (UserInfo) req.getSession().getAttribute(Constants.SESSION_USER_INFO);
        if (userInfo == null) {
            if (authentication.getDetails() instanceof WebAuthenticationDetails) {
                // 谷歌登陆
                WebAuthenticationDetails oAuth2AuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                OAuth2User oAuth2User = token.getPrincipal();
                Map<String,Object> UserDetail=oAuth2User.getAttributes();
                //组装用户信息
                userInfo = new UserInfo();
                userInfo.setLoginId(oAuth2AuthenticationDetails.getSessionId());
                userInfo.setUserName((String)UserDetail.get("name"));
                userInfo.setEmail((String)UserDetail.get("email"));
            }

            //操作日志
            OperationLogDTO operationLogDTO = new OperationLogDTO();
            operationLogDTO.setUsername(userInfo.getUserName());
            operationLogDTO.setEmail(userInfo.getEmail());
            operationLogDTO.setOperation(0);
            operationLogDTO.setOperationTime(System.currentTimeMillis());
            operationLogDTO.setPartner("test");
            operationLogService.addOperationLog(operationLogDTO);

            //存储用户信息到redis session
            req.getSession().setAttribute(Constants.SESSION_USER_INFO, userInfo);
        }
        return userInfo;
    }


    @Override
    public void destroy() {

    }




    public SystemConfig getSystemConfig() {
        return systemConfig;
    }

    public void setSystemConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }


    public void setOperationLogService(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }
}
