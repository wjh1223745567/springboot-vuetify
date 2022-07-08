package com.iotinall.framework.modules.admin.dto.sysrole;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author WJH
 * @date 2019/12/1316:33
 */
@Data
public class SysRoleReq {

    private Long id;

    @NotBlank
    @Length(max = 32)
    private String name; // 角色名称

    @Length(max = 1024)
    private String remark;  // 备注

}
