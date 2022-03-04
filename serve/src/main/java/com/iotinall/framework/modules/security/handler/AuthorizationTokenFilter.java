package com.iotinall.framework.modules.security.handler;

import com.iotinall.framework.modules.security.module.SecurityUserDetails;
import com.iotinall.framework.modules.security.module.SecurityUserModel;
import com.iotinall.framework.modules.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WJH
 * @date 2019/12/1311:07
 */
@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisTemplate<String, SecurityUserModel> redisTemplate;

    @Value("${tokenTimeOut:600}")
    private Integer tokenTimeOut;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = SecurityUtil.getToken(request);
        if(token != null) { // && token != null && !token.isEmpty()
            SecurityUserModel model = this.redisTemplate.opsForValue().get(token);
            if(model != null){
                redisTemplate.expire(token, tokenTimeOut, TimeUnit.MINUTES);
                UserDetails userDetails = _getUser(model);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }

    private UserDetails _getUser(SecurityUserModel userDetails) {
        List<GrantedAuthority> collect = userDetails.getAuthorizes() != null ? userDetails.getAuthorizes().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()) : new ArrayList<>();

        SecurityUserDetails securityUserDetails = new SecurityUserDetails(userDetails, collect);
        return securityUserDetails;
    }
}
