package com.iotinall.framework.modules.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iotinall.framework.common.exception.BizException;
import com.iotinall.framework.modules.admin.dto.sysrole.*;
import com.iotinall.framework.modules.admin.entity.OrgMember;
import com.iotinall.framework.modules.admin.entity.SysMemberRole;
import com.iotinall.framework.modules.admin.entity.SysRole;
import com.iotinall.framework.modules.admin.entity.SysRolePermission;
import com.iotinall.framework.modules.admin.mapper.SysRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色资源管理
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRolePermissionService sysRolePermissionService;

    @Resource
    private SysMemberRoleService sysMemberRoleService;

    @Resource
    private OrgMemberService orgMemberService;

    /**
     * 列表
     * @param condition
     * @return
     */
    public IPage<SysRoleDto> list(Page<SysRole> page, SysRoleCondition condition) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(condition.getSearch()), SysRole::getName, condition.getSearch());
        IPage<SysRole> iPage = this.sysRoleMapper.selectPage(page, queryWrapper);
        List<Long> roleId = iPage.getRecords().stream().map(SysRole::getId).collect(Collectors.toList());
        Map<Long, List<String>> map = this.sysRolePermissionService.findPermissionByRoleIds(roleId);
        return iPage.convert(item -> {
            SysRoleDto sysRoleDto = new SysRoleDto()
                    .setId(item.getId())
                    .setName(item.getName())
                    .setPermissions(map.get(item.getId()))
                    .setRemark(item.getRemark());
            return sysRoleDto;
        });
    }

    public Long saveData(SysRoleReq roleReq){
        SysRole sysRole = new SysRole();
        sysRole.setName(roleReq.getName())
                .setRemark(roleReq.getRemark())
                .setId(roleReq.getId());
        super.saveOrUpdate(sysRole);
        return sysRole.getId();
    }

    public void deleted(Collection<Long> ids){
        if(CollectionUtils.isEmpty(ids)){
            throw new BizException("", "请选择要删除的角色");
        }
        this.sysMemberRoleService.deletedByRoleIds(ids);
        this.sysRolePermissionService.deletedByRoleIds(ids);
    }

    /**
     * 获取角色人员信息
     * @param condition
     */
    public SysRoleAuthDto roleUser(SysRoleUserCondition condition){
        Set<Long> set = this.sysMemberRoleService.findMemberIds(condition.getRoleId());
        List<OrgMember> noAuthMembers = this.orgMemberService.searchUser(condition.getNoauthsearch(), set);
        List<OrgMember> authMembers = this.orgMemberService.searchUserByIds(condition.getAuthsearch(), set);
        SysRoleAuthDto authDto = new SysRoleAuthDto()
                .setNoAuthUser(noAuthMembers.stream().map(item -> {
                    SysRoleUserDto roleUserDto = new SysRoleUserDto()
                            .setId(item.getId())
                            .setName(item.getName())
                            .setMobile(item.getMobile())
                            .setWorkNum(item.getWorkNum());
                    return roleUserDto;
                }).collect(Collectors.toList()))
                .setAuthUser(authMembers.stream().map(item -> {
                    SysRoleUserDto roleUserDto = new SysRoleUserDto()
                            .setId(item.getId())
                            .setName(item.getName())
                            .setWorkNum(item.getWorkNum())
                            .setMobile(item.getMobile());
                    return roleUserDto;
                }).collect(Collectors.toList()));
        return authDto;
    }

    /**
     * 移除用户角色
     */
    public void removeRoleUser(SysRoleUserEdit edit){
        this.sysMemberRoleService.deletedByRoleMember(edit.getRoleId(), edit.getMemberIds());
    }

    public void addRoleUser(SysRoleUserEdit edit){
        this.sysMemberRoleService.addRoleMember(edit.getRoleId(), edit.getMemberIds());
    }
    /**
     * 更新权限
     */
    public void updatePermissions(SysRolePermissionReq req){
        this.sysRolePermissionService.deletedPermission(Collections.singleton(req.getId()));
        if(!CollectionUtils.isEmpty(req.getPermissions())){
            List<SysRolePermission> sysRolePermissions = req.getPermissions().stream().filter(StringUtils::isNotBlank).map(String::trim)
                    .map(item -> new SysRolePermission().setPermission(item).setRoleId(req.getId())).collect(Collectors.toList());
            this.sysRolePermissionService.saveBatch(sysRolePermissions);
        }

    }

    /**
     * 添加单个人员角色
     */
    public void addSimpleMember(Long memberId, List<Long> roleIds) {
        //清除历史角色
        this.sysMemberRoleService.deletedByMemberId(memberId);

        //添加新角色
        if (roleIds != null && !roleIds.isEmpty()){
            for (Long roleId : roleIds) {
                SysMemberRole memberRole = new SysMemberRole()
                        .setRoleId(roleId)
                        .setMemberId(memberId);
                this.sysMemberRoleService.save(memberRole);
            }
        }
    }

    public List<String> getPermission(Long memberId){
        Set<Long> roleids = this.sysMemberRoleService.findRoleIds(memberId);
        return getPermission(roleids);
    }

    /**
     * 角色权限资源
     * @param roleIds
     * @return
     */
    public List<String> getPermission(Collection<Long> roleIds){
        if(CollectionUtils.isEmpty(roleIds)){
            return new ArrayList<>(0);
        }
        List<SysRolePermission> permissions = this.sysRolePermissionService.findByRoleIds(roleIds);
        return permissions.stream().map(SysRolePermission::getPermission).collect(Collectors.toList());
    }
}
