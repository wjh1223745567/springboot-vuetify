package com.iotinall.framework.modules.security.util;

import com.iotinall.framework.modules.security.module.SecurityUserDetails;
import com.iotinall.framework.modules.security.module.SecurityUserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * time: 5/11/2019
 *
 * @author xin-bing
 */
public class SecurityUtil {

    private static final String HEADER_NAME = "X-Authorization";
    private static final String COOKIE_NAME = "X-Authorization";

    /**
     * description: 获取当前用户的userDetails
     * time: 5/27/2019 10:44
     * @author xin-bing
     * @version 1.0
     * @param
     * @return com.rtsm.alarmserver.security.model.SecurityUserDetails
     */
    public static SecurityUserDetails getCurrUserDetails() {
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails;
    }

    /**
     * description: 获取当前登录用户的model
     * time: 5/27/2019 10:46
     * @author xin-bing
     * @version 1.0
     * @param
     * @return com.rtsm.alarmserver.security.model.SecurityUserModel
     */
    public static SecurityUserModel getCurrUserModel() {
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getSecurityUserModel();
    }
    /**
     * description: 获取当前用户名
     * time: 5/12/2019 19:08
     * @author xin-bing
     * @version 1.0
     * @param
     * @return java.lang.String
     */
    public static String getCurrUserName() {
        return getCurrUserModel().getUserName();
    }

    /**
     * description: 获取当前用户名
     * time: 5/12/2019 19:08
     * @author xin-bing
     * @version 1.0
     * @param
     * @return java.lang.String
     */
    public static Long getCurrUserID() {
        return getCurrUserModel().getId();
    }

    /**
     * description: 获取token
     * time: 5/26/2019 19:50
     * @author xin-bing
     * @version 1.0
     * @param request
     * @return java.lang.String
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_NAME);
        if(StringUtils.isBlank(token)) { // 当从header中拿不到token再去cookie中拿；一般请求cookie都放在header中；下载请求是无法指定header的，所以做一个保险
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for(Cookie cookie: cookies) {
                    if(COOKIE_NAME.equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return token;
    }

}
