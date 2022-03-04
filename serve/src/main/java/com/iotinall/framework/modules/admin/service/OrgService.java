package com.iotinall.framework.modules.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iotinall.framework.common.exception.BizException;
import com.iotinall.framework.modules.admin.dto.org.OrgEdit;
import com.iotinall.framework.modules.admin.dto.org.OrgReq;
import com.iotinall.framework.modules.admin.dto.org.OrgTreeDto;
import com.iotinall.framework.modules.admin.entity.Org;
import com.iotinall.framework.modules.admin.mapper.OrgMapper;
import com.iotinall.framework.modules.admin.mapper.OrgMemberMapper;
import com.iotinall.framework.util.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrgService extends ServiceImpl<OrgMapper, Org> {

    @Resource
    private OrgMapper orgMapper;

    @Resource
    private OrgMemberMapper orgMemberMapper;

    private final String fullCodeSplit = ",";

    /**
     * 获取树结构
     * @param name
     * @return
     */
    public List<OrgTreeDto> findTree(String name){
        QueryWrapper<Org> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(Org::getCreateTime);
        queryWrapper.lambda().like(StringUtils.isNotBlank(name), Org::getName, name);
        return this.coverDtoList(this.orgMapper.selectList(queryWrapper));
    }

    private List<OrgTreeDto> coverDtoList(List<Org> orgs){
        Map<Long, OrgTreeDto> map = new ConcurrentHashMap<>();
        for (Org org : orgs) {
            OrgTreeDto treeDto = new OrgTreeDto()
                    .setId(org.getId())
                    .setName(org.getName())
                    .setRemark(org.getRemark())
                    .setPid(org.getPid());
            map.put(org.getId(), treeDto);
        }

        List<OrgTreeDto> res = new ArrayList<>();
        map.forEach((orgId, treeDto) -> {
            if(treeDto.getPid() == null || !map.containsKey(treeDto.getPid())){
                res.add(treeDto);
            }else if(map.containsKey(treeDto.getPid())){
                map.get(treeDto.getPid()).getChildren().add(treeDto);
            }
        });

        return res;
    }

    public Long save(OrgReq req) {
        Org org = new Org();
        Set<Org> childrens = null;
        if(req.getId() != null){
            org = this.orgMapper.selectById(req.getId());
            childrens = this.orgMapper.findAllChildrenNoSelf(req.getId());
        }
        org.setName(req.getName())
            .setMemberCount(0)
            .setCode(org.getCode() == null ? RandomStringUtils.randomAlphanumeric(5) : org.getCode())
            .setPid(req.getPid())
            .setRemark(req.getRemark())
            .setId(req.getId());
        if(req.getId() != null && Objects.equals(req.getId(), req.getPid())){
            throw new BizException("", "不能选择自己为自己的父部门");
        }

        setFullCodeAndLeave(org);
        if(!CollectionUtils.isEmpty(childrens)){
            for (Org children : childrens) {
                setFullCodeAndLeave(children);
            }
        }
        return org.getId();
    }


    private void setFullCodeAndLeave(Org org){
        String fullCode;
        if(org.getPid() != null && (fullCode = this.orgMapper.findFullCode(org.getPid())) != null ){
            fullCode = fullCode + org.getCode() + fullCodeSplit;
        }else{
            fullCode = org.getCode() + fullCodeSplit;
        }
        org.setFullCode(fullCode);
        org.setLeaveRank((int)Arrays.stream(fullCode.split(fullCodeSplit)).filter(StringUtils::isNotBlank).count());
        super.saveOrUpdate(org);
    }

    /**
     * 批量删除
     */
    public void deleted(Long id){
        if(id == null){
           throw new BizException("", "请选择需要删除的部门");
        }
        Set<Long> childrenIds = this.orgMapper.findChildrenIds(id);
        if(CollectionUtils.isEmpty(childrenIds)){
            throw new BizException("", "未找到要删除的部门");
        }
        Integer count = this.orgMemberMapper.countByOrgIds(SqlUtils.getInStr(childrenIds));
        if(count > 0){
            throw new BizException("", "当前部门存在人员信息，无法删除");
        }

        int delCount = this.orgMapper.deleteById(id);
        if(delCount <= 0){
            throw new BizException("", "删除失败");
        }
    }

    public Map<Long, Org> findIdsMap(Collection<?> collection){
        if(collection.isEmpty()){
            return new HashMap<>();
        }
        QueryWrapper<Org> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Org::getId, collection);
        List<Org> orgs = super.list(queryWrapper);
        Map<Long, Org> result = new HashMap<>();
        for (Org org : orgs) {
            result.put(org.getId(), org);
        }
        return result;
    }
}
