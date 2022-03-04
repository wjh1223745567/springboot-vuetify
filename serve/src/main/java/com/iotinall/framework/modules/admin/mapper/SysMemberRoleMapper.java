package com.iotinall.framework.modules.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iotinall.framework.modules.admin.entity.SysMemberRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface SysMemberRoleMapper extends BaseMapper<SysMemberRole> {

    @Select("select o.role_id from sys_member_role o where o.member_id = '${memberId}' ")
    Set<Long> findRoleIds(@Param("memberId") Long memberId);

    @Select("select o.member_id from sys_member_role o where o.role_id = '${roleId}'")
    Set<Long> findMemberIds(@Param("roleId") Long roleId);

}
