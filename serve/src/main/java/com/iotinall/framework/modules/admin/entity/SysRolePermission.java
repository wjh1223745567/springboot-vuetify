package com.iotinall.framework.modules.admin.entity;


import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色对应权限字段
 */
@Data
@Table
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysRolePermission extends BaseCreateEntity{

    @Column(isNull = false, comment = "角色ID")
    private Long roleId;

    @Column(isNull = false, length = 200, comment = "权限资源")
    private String permission;

}
