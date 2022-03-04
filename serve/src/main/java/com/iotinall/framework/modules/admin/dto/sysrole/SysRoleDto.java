package com.iotinall.framework.modules.admin.dto.sysrole;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author WJH
 * @date 2019/12/2711:19
 */
@Data
@Accessors(chain = true)
public class SysRoleDto {

    private Long id; // 主键

    private String name; // 角色名称



    private String remark;  // 备注

    /**
     * 权限
     */
    private List<String> permissions;
    /**
     * 人员信息
     */
    private String users;

}
