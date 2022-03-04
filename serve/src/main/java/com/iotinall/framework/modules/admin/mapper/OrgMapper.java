package com.iotinall.framework.modules.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iotinall.framework.modules.admin.entity.Org;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface OrgMapper extends BaseMapper<Org> {

    /**
     * 查询父ID
     * @param id
     * @return
     */
    @Select("select pid from org where id = ${id}")
    Long findParentId(@Param("id") Long id);

    @Select("select * from org where pid = ${orgId} and deleted <> 1")
    List<Org> findChildren(@Param("orgId") Long orgId);

    @Select("select count(0) from org where pid = ${orgId} and deleted <> 1")
    Integer countChildren(@Param("orgId") Long orgId);

    @Select("select * from org where code = '${code}' and deleted <> 1")
    Org findByCode(@Param("code") String code);

    /**
     * 查询多个
     * @param codes
     * @return
     */
    @Select("select * from org where code in ('${codes}') and deleted <> 1 order by leave_rank")
    List<Org> findByCodes(@Param("codes") String codes);

    @Select("select leave_rank from org where id = '${id}' and deleted <> 1")
    Integer findLeave(@Param("id") Long id);

    /**
     * 根据部门ID和级别查询部门
     * @param leave
     * @param orgids
     * @return
     */
    @Select("select id from org where leave_rank = '${leave}' and id in (${orgIds}) and deleted <> 1")
    List<Long> findOrgByLeave(@Param("leave") Integer leave, @Param("orgIds") String orgids);

    @Select("select * from org where leave_rank = '${leave}' and deleted <> 1")
    List<Org> findOrgLeave(@Param("leave") Integer leave);

    /**
     * 查询所有部门ID
     * @return
     */
    @Select("select id from org where deleted <> 1")
    List<Long> findAllOrgId();

    /**
     * 查询fullcode
     * @param id
     * @return
     */
    @Select("select full_code from org where id = '${id}'")
    String findFullCode(@Param("id") Long id);

    @Select("select org2.id FROM `org` org, org org2 where org.id = '${id}' and org2.full_code like CONCAT(org.full_code, '%') and org2.deleted <> 1")
    Set<Long> findChildrenIds(@Param("id") Long id);

    @Select("select org2.* FROM `org` org, org org2 where org.id = '${id}' and org2.full_code like CONCAT(org.full_code, '%') and org2.deleted <> 1 order by org2.leave_rank")
    Set<Org> findAllChildren(@Param("id") Long id);

    @Select("select org2.* FROM `org` org, org org2 where org.id = '${id}' and org2.id <> '${id}' and org2.full_code like CONCAT(org.full_code, '%') and org2.deleted <> 1 order by org2.leave_rank")
    Set<Org> findAllChildrenNoSelf(@Param("id") Long id);
}
