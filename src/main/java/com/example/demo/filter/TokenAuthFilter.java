package com.example.demo.filter;

import com.sun.istack.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import util.RespUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * 认证解析token过滤器
 * OncePerRequestFilter可以保证一次外部请求，只执行一次过滤方法，对于服务器内部之间的forward等请求，不会再次执行过滤方法。
 */
public class TokenAuthFilter extends OncePerRequestFilter {

    public TokenAuthFilter(){

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws IOException, ServletException {
        logger.info("当前接口调用为uri:"+request.getRequestURI());
        //如果是谷歌登录回调接口，直接放行
        if("/googleLogin".equals(request.getRequestURI()) || "/googleLoginCallback".equals(request.getRequestURI()) || "/getTokenByCode".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里

        return null;
    }
}

