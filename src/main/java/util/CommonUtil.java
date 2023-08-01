package util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public class CommonUtil {
    /**
     * 获取请求的时区
     *
     * @return
     */
    public static String getRequestTimeZone(HttpServletRequest request) {
        //优先获取请求头的timezone，再获取cookie的timeZone（后面下线）
        String result = request.getHeader("timezone");
        if(StringUtils.isBlank(result)) {
            return getCookie(request, "timeZone");
        }else{
            return result;
        }
    }
    public static String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        String cookieValue = null;
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if (StringUtils.equals(cookieName, cookie.getName())) {
                cookieValue = cookie.getValue();
                break;
            }
        }
        return cookieValue;
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String cookieValue, int cookieMaxAge, String domain,boolean secure) {
        if (StringUtils.isBlank(cookieValue)) {
            return;
        }
        try {
            Cookie[] cookies = request.getCookies();

            // 避免重置种植
            if (cookies != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (StringUtils.equals(cookieName, cookie.getName())) {
                        if (StringUtils.equals(cookieValue, cookie.getValue())) {
                            return;
                        }
                    }
                }
            }
            String domainString = "";
            if(StringUtils.isNotBlank(domain)) {
                domainString = "Domain="+domain+";";
            }
            //解决chrome80版本以上跨域设置cookie时无法生效的问题
            response.addHeader("Set-Cookie",cookieName+"="+cookieValue+";" + "Path=/;Max-Age="+cookieMaxAge+"; "+domainString+(secure?" SameSite=None;Secure":""));
//            Cookie cookieLastUrl = new Cookie(cookieName,writeBackCookieValue);
//            cookieLastUrl.setPath("/");
//            cookieLastUrl.setDomain("");
//            cookieLastUrl.setHttpOnly(false);
//            cookieLastUrl.setMaxAge(COOKIE_MAX_AGE);
//            cookieLastUrl.setSecure(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,String cookieName) {
        Cookie[] cookies = request.getCookies();

        // 避免重置种植
        if (cookies != null) {
            for (Cookie cookie : request.getCookies()) {
                if (StringUtils.equals(cookieName, cookie.getName())) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
}
