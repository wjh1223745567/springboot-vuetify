package com.iotinall.framework.modules.admin.dto.orgmember;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrgMemberPwdReq {

    @NotBlank
    private String oldpwd;

    @NotBlank
    private String newpwd;

}
