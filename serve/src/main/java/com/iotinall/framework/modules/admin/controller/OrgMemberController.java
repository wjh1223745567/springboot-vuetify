package com.iotinall.framework.modules.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iotinall.framework.common.model.ResultDTO;
import com.iotinall.framework.modules.admin.dto.orgmember.OrgMemberCondition;
import com.iotinall.framework.modules.admin.dto.orgmember.OrgMemberReq;
import com.iotinall.framework.modules.admin.entity.OrgMember;
import com.iotinall.framework.modules.admin.service.OrgMemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 组织人员管理
 * @author WJH
 * @date 2019/12/1315:24
 */
@RestController
@RequestMapping(value = "org_member")
public class OrgMemberController {

    @Resource
    private OrgMemberService orgMemberService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_MEMBER_ALL')")
    public ResultDTO<?> listByPage(OrgMemberCondition condition, Page<OrgMember> page){
        return ResultDTO.success(this.orgMemberService.page(condition,page));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_MEMBER_ALL')")
    public ResultDTO<?> save(@Valid OrgMemberReq req){
        return ResultDTO.success(this.orgMemberService.save(req));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_MEMBER_ALL')")
    public ResultDTO deleted(@RequestParam(value = "ids[]") List<Long> ids){
        this.orgMemberService.deleted(ids);
        return ResultDTO.success();
    }


}
