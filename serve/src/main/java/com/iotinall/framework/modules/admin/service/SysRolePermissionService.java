package com.iotinall.framework.modules.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iotinall.framework.modules.admin.entity.SysRolePermission;
import com.iotinall.framework.modules.admin.mapper.SysRolePermissionMapper;
import com.iotinall.framework.util.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRolePermissionService extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> {

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    public void deletedByRoleIds(Collection<Long> ids){
        if(!CollectionUtils.isEmpty(ids)){
            QueryWrapper<SysRolePermission> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(SysRolePermission::getRoleId, ids);
            this.sysRolePermissionMapper.delete(queryWrapper);
        }
    }

    public List<SysRolePermission> findByRoleIds(Collection<Long> roleIds){
        return this.sysRolePermissionMapper.findByRoleIds(SqlUtils.getInStr(roleIds));
    }

    /**
     * 查找权限资源
     * @param roleIds
     * @return
     */
    public Map<Long, List<String>> findPermissionByRoleIds(Collection<Long> roleIds){
        if(CollectionUtils.isEmpty(roleIds)){
            return new HashMap<>();
        }
        List<SysRolePermission> permissions = this.sysRolePermissionMapper.findByRoleIds(SqlUtils.getInStr(roleIds));
        Map<Long, List<String>> map = new HashMap<>();
        roleIds.forEach(item -> {
            map.put(item, new ArrayList<>());
        });
        for (SysRolePermission permission : permissions) {
            map.get(permission.getRoleId()).add(permission.getPermission());
        }
        return map;
    }

    /**
     * 清理权限
     * @param roleIds
     */
    public void deletedPermission(Collection<Long> roleIds){
        if(!CollectionUtils.isEmpty(roleIds)){
            QueryWrapper<SysRolePermission> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(SysRolePermission::getRoleId, roleIds);
            this.sysRolePermissionMapper.delete(queryWrapper);
        }
    }
}

