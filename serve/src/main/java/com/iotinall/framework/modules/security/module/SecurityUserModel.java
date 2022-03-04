package com.iotinall.framework.modules.security.module;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * time: 5/26/2019
 *
 * @author xin-bing
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class SecurityUserModel implements Serializable {
    private Long id;
    /**
     * 用户名称
     */
    private String userName;

    private String token;

    private String name;

    private String mobile;

    /**
     * 照片
     */
    private String avatar;

    private Long orgId;

    private String orgName;

    /**
     * 权限
     */
    private List<String> authorizes;

}
