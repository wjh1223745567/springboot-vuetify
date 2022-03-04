package com.iotinall.framework.modules.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iotinall.framework.common.model.ResultDTO;
import com.iotinall.framework.modules.admin.dto.sysrole.*;
import com.iotinall.framework.modules.admin.entity.SysRole;
import com.iotinall.framework.modules.admin.service.SysRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 角色
 */
@RestController
@RequestMapping(value = "sys_role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SYS_ROLE_ALL')")
    public ResultDTO<?> list(Page<SysRole> page, SysRoleCondition condition){
        return ResultDTO.success(this.sysRoleService.list(page, condition));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SYS_ROLE_ALL')")
    public ResultDTO<?> add(@Valid SysRoleReq req){
        return ResultDTO.success(this.sysRoleService.saveData(req));
    }

    @PostMapping(value = "update_permissions")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYS_ROLE_ALL')")
    public ResultDTO<?> updatePermissions(@Valid SysRolePermissionReq req){
        this.sysRoleService.updatePermissions(req);
        return ResultDTO.success();
    }

    @PostMapping(value = "remove_role_user")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYS_ROLE_ALL')")
    public ResultDTO<?> removeRoleUser(@Valid SysRoleUserEdit edit){
        this.sysRoleService.removeRoleUser(edit);
        return ResultDTO.success();
    }

    @PostMapping(value = "add_role_user")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYS_ROLE_ALL')")
    public ResultDTO<?> addRoleUser(@Valid SysRoleUserEdit edit){
        this.sysRoleService.addRoleUser(edit);
        return ResultDTO.success();
    }

    /**
     * 获取用户角色
     * @param condition
     * @return
     */
    @GetMapping(value = "get_role_user")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYS_ROLE_ALL')")
    public ResultDTO<?> getRoleUser(@Valid SysRoleUserCondition condition){
        return ResultDTO.success(this.sysRoleService.roleUser(condition));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SYS_ROLE_ALL')")
    public ResultDTO<?> deleted(@RequestParam(value = "ids[]") List<Long> ids){
        this.sysRoleService.deleted(ids);
        return ResultDTO.success();
    }
}
