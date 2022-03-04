package com.iotinall.framework.modules.admin.dto.sysrole;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class SysRoleUserEdit {

    @NotNull
    private Long roleId;

    @NotEmpty
    private Set<Long> memberIds;
}
