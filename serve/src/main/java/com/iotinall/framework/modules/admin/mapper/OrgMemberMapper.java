package com.iotinall.framework.modules.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iotinall.framework.modules.admin.entity.OrgMember;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface OrgMemberMapper extends BaseMapper<OrgMember> {

    @Select("select count(*) from org_member where org_id in (${orgIds}) and deleted <> 1")
    Integer countByOrgIds(@Param("orgIds") String orgIds);

    /**
     * 根据所有人员信息
     * @param id
     * @return
     */
    @Select("select * from org_member where id = '${id}'")
    OrgMember findAllById(@Param("id") Long id);

    @Select("select * from org_member where work_num = '${workNum}'")
    OrgMember findAllByWorkNum(@Param("workNum") String workNum);

    /**
     * 修改密码
     * @param pwd
     * @param id
     * @return
     */
    @Update("update org_member set pwd = '${pwd}' where id = '${id}'")
    Integer updatePwd(@Param("pwd") String pwd, @Param("id") Long id);

}
