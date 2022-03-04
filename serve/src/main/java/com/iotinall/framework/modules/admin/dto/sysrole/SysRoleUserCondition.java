package com.iotinall.framework.modules.admin.dto.sysrole;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SysRoleUserCondition {

    private String noauthsearch;

    private String authsearch;

    @NotNull
    private Long roleId;

}
