package com.iotinall.framework.modules.admin.dto.sysrole;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SysRoleUserDto {

    private Long id;

    private String name;

    private String mobile;

    private String workNum;

}
