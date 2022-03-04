package com.iotinall.framework.modules.admin.controller;

import com.iotinall.framework.common.model.ResultDTO;
import com.iotinall.framework.modules.admin.dto.org.OrgEdit;
import com.iotinall.framework.modules.admin.dto.org.OrgReq;
import com.iotinall.framework.modules.admin.service.OrgService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author bingo
 * @date 12/11/2019 14:39
 */
@RestController
@RequestMapping(value = "org")
public class OrgController {

    @Resource
    private OrgService orgService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_MEMBER_ALL')")
    public ResultDTO<?> addOrg(@Valid OrgReq req){
        return ResultDTO.success( this.orgService.save(req));
    }


    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_MEMBER_ALL')")
    public ResultDTO deleted(@RequestParam(value = "ids[]") List<Long> ids){
        for (Long id : ids) {
            this.orgService.deleted(id);
        }
        return  ResultDTO.success();
    }

    /**
     * 查询树结构
     */
    @GetMapping(value = "find_tree")
    public ResultDTO findTree(String name){
        return ResultDTO.success(this.orgService.findTree(name));
    }

}
