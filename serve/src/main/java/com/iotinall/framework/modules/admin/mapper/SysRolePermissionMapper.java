package com.iotinall.framework.modules.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iotinall.framework.modules.admin.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    @Select("select * from sys_role_permission where role_id in(${roleIds}) ")
    List<SysRolePermission> findByRoleIds(@Param("roleIds") String roleIds);

}
