package com.iotinall.framework.modules.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iotinall.framework.modules.admin.entity.OrgMember;
import com.iotinall.framework.modules.admin.entity.SysMemberRole;
import com.iotinall.framework.modules.admin.mapper.SysMemberRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 人员角色
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysMemberRoleService extends ServiceImpl<SysMemberRoleMapper, SysMemberRole> {

    @Resource
    private SysMemberRoleMapper sysMemberRoleMapper;

    public void deletedByRoleIds(Collection<Long> ids){
        if(!CollectionUtils.isEmpty(ids)){
            QueryWrapper<SysMemberRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(SysMemberRole::getRoleId, ids);
            this.sysMemberRoleMapper.delete(queryWrapper);
        }
    }

    public void deletedByMemberId(Long memberId){
        if(memberId != null){
            QueryWrapper<SysMemberRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysMemberRole::getMemberId, memberId);
            this.sysMemberRoleMapper.delete(queryWrapper);
        }
    }

    public void deletedByMemberId(Collection<Long> memberIds){
        if(!CollectionUtils.isEmpty(memberIds)){
            for (Long memberId : memberIds) {
                deletedByMemberId(memberId);
            }
        }
    }

    public void deletedByRoleMember(Long roleId, Collection<Long> memberIds){
        if(roleId != null && !CollectionUtils.isEmpty(memberIds)){
            QueryWrapper<SysMemberRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(SysMemberRole::getRoleId, roleId);
            queryWrapper.lambda().in(SysMemberRole::getMemberId, memberIds);
            this.sysMemberRoleMapper.delete(queryWrapper);
        }
    }

    public void addRoleMember(Long roleId, Collection<Long> memberIds){
        if(roleId != null && !CollectionUtils.isEmpty(memberIds)){
            deletedByRoleMember(roleId, memberIds);
            List<SysMemberRole> sysMemberRoles = memberIds.stream().map(item -> new SysMemberRole().setMemberId(item).setRoleId(roleId)).collect(Collectors.toList());
            super.saveBatch(sysMemberRoles);
        }

    }

    public Set<Long> findRoleIds(Long memberId){
        if(memberId == null){
            return new HashSet<>();
        }
        return sysMemberRoleMapper.findRoleIds(memberId);
    }

    public Set<Long> findMemberIds(Long roleId){
        if(roleId == null){
            return new HashSet<>();
        }
        return sysMemberRoleMapper.findMemberIds(roleId);
    }

}
