package com.example.demo.filter;



import com.example.demo.enums.ResultCodeEnum;
import com.example.demo.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.StringUtils;

import org.springframework.security.authentication.AuthenticationServiceException;
import util.CommonUtil;
import util.Constants;
import util.RespUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;


@Slf4j
public class DomainSwitchFilter implements Filter {
    private static final Pattern IPV4_REGEX =
            Pattern.compile(
                    "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");



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

        String url = req.getRequestURL().toString();
        if (StringUtils.startsWith(url, "http://123")
                || StringUtils.startsWith(url, "https://321")) {
            String lastUrl = CommonUtil.getCookie(req, Constants.COOKIE_LAST_URL);
            if (StringUtils.isNotBlank(lastUrl) && !StringUtils.startsWith(lastUrl, "http://")
                    && !StringUtils.startsWith(lastUrl, "https://")) {
                //通过cookie转发
                resp.sendRedirect(lastUrl);
            } else {
                //通过ip国家转发
                String ip = getClientIp(req);
                String country = null;
                if (StringUtils.isNotBlank(ip)) {
                    country = "ip"+country;
                }
                log.info("x-forwarded-for:" + req.getHeader("x-forwarded-for") + " country:" + country);
                String redirectDomain = "wt.test.com";
                if (StringUtils.containsIgnoreCase(country, "中国")||StringUtils.containsIgnoreCase(country, "China")) {
                    redirectDomain = "wt.test.com";
                }
                String redirectURL = StringUtils.replace(url, "portal.trustdecision.com", redirectDomain);
                if(redirectURL.startsWith("http://127.0.0.1:8080/")){
                    redirectURL="http://127.0.0.1:8080/login";
                }
//                resp.sendRedirect(redirectURL);
            }
        } else if (StringUtils.startsWith(url, "https://docs.test.com")
                || StringUtils.startsWith(url, "https://doc.test.com")
                || StringUtils.startsWith(url, "http://docs.test.com")
                || StringUtils.startsWith(url, "http://doc.test.com")) {
            //通过ip国家转发
            String ip = getClientIp(req);
            String country = null;
            if (StringUtils.isNotBlank(ip)) {
                country = ip+"country";
            }
            log.info("x-forwarded-for:" + req.getHeader("x-forwarded-for") + " country:" + country);
            String redirectDomain = "en-doc.test.com";
            if (StringUtils.containsIgnoreCase(country, "中国")||StringUtils.containsIgnoreCase(country, "China")) {
                redirectDomain = "cn-doc.test.com";
            }
            String redirectURL;
            if (StringUtils.startsWith(url, "http://docs.test.com")
                    || StringUtils.startsWith(url, "https://docs.test.com")) {
                redirectURL = StringUtils.replace(url, "docs.test.com", redirectDomain);
            } else {
                redirectURL = StringUtils.replace(url, "doc.test.com", redirectDomain);
            }

            resp.sendRedirect(redirectURL);
        } else {
            try {
                filterChain.doFilter(req, servletResponse);
            } catch (SerializationException e) {
                log.error(e.getMessage(), e);
                //当切换环境时，可能造成序列化失败，清理session重新登录
                CommonUtil.deleteCookie(req, resp, Constants.COOKIE_SESSION);
                CommonUtil.deleteCookie(req, resp, Constants.COOKIE_PARTNER_CODE);
                resp.sendRedirect("/login");
            } catch (BizException e) {
                log.error(e.getMessage(), e);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                if (e.getCodeEnum() != null) {
                    resp.getWriter().print(RespUtil.getVO(e.getCodeEnum()));
                } else {
                    resp.getWriter().print(RespUtil.getVO(ResultCodeEnum.CODE_201));
                }
            } catch (AuthenticationServiceException e) {
                log.error(e.getMessage(), e);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().print(RespUtil.getVO(ResultCodeEnum.CODE_403));
            }
        }

    }

    /**
     * 先取左边第一位不是本地ip的ip
     * 获取不到，则取remoteAddress
     * @param request
     * @return
     */
    private static String getClientIp(HttpServletRequest request) {
        String xffIp = request.getHeader("x-forwarded-for");
        if (!StringUtils.isEmpty(xffIp)) {
            String[] values = xffIp.split(",");
            int length = values.length;
            String result = null;
            for (int i = 0; i < length; i++) {
                String ip = values[i].trim();
                if (!isLocalIp(ip) && IPV4_REGEX.matcher(ip).matches()) {
                    result = ip;
                    break;
                }
            }
            if (StringUtils.isEmpty(result)) {
                result = values[0].trim();
            }
            return result;
        }
        return request.getRemoteAddr();
    }

    /**
     * @param ip
     * @return
     */
    private static boolean isLocalIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isLinkLocalAddress() || address.isSiteLocalAddress();
        } catch (UnknownHostException e) {
        }
        return false;
    }

    @Override
    public void destroy() {

    }

}
