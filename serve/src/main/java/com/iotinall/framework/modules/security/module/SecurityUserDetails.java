package com.iotinall.framework.modules.security.module;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * time: 5/26/2019
 *
 * @author xin-bing
 */
public class SecurityUserDetails implements UserDetails {
    private SecurityUserModel securityUserModel;

    List<GrantedAuthority> authorities;

    public SecurityUserDetails(SecurityUserModel securityUserModel, List<GrantedAuthority> authorities) {
        this.securityUserModel = securityUserModel;
        this.authorities = authorities;
    }



    public SecurityUserModel getSecurityUserModel() {
        return this.securityUserModel;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return securityUserModel.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
