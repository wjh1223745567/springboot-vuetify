package com.iotinall.framework.modules.admin.dto.sysrole;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SysRoleAuthDto {

    private List<SysRoleUserDto> noAuthUser;

    private List<SysRoleUserDto> authUser;

}
