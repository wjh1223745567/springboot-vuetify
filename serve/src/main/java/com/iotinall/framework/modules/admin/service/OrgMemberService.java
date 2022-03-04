package com.iotinall.framework.modules.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iotinall.framework.common.exception.BizException;
import com.iotinall.framework.constants.enums.GenderEnum;
import com.iotinall.framework.modules.admin.dto.orgmember.OrgMemberCondition;
import com.iotinall.framework.modules.admin.dto.orgmember.OrgMemberDto;
import com.iotinall.framework.modules.admin.dto.orgmember.OrgMemberReq;
import com.iotinall.framework.modules.admin.entity.Org;
import com.iotinall.framework.modules.admin.entity.OrgMember;
import com.iotinall.framework.modules.admin.mapper.OrgMapper;
import com.iotinall.framework.modules.admin.mapper.OrgMemberMapper;
import com.iotinall.framework.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 人员管理
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrgMemberService extends ServiceImpl<OrgMemberMapper, OrgMember> {

    @Resource
    private OrgMemberMapper orgMemberMapper;

    @Resource
    private OrgMapper orgMapper;

    @Resource
    private OrgService orgService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private SysMemberRoleService sysMemberRoleService;


    public IPage<OrgMemberDto> page(OrgMemberCondition condition, Page<OrgMember> page){
        QueryWrapper<OrgMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(condition.getOrgId() != null, OrgMember::getOrgId, orgMapper.findChildrenIds(condition.getOrgId()));
        if(StringUtils.isNotBlank(condition.getSearch())){
            queryWrapper.lambda().and(wrapper -> wrapper.like(OrgMember::getName, condition.getSearch())
                .or().like(OrgMember::getWorkNum, condition.getSearch())
                .or().like(OrgMember::getMobile, condition.getSearch())
            );
        }
        IPage<OrgMember> pageList = this.orgMemberMapper.selectPage(page, queryWrapper);
        Set<Long> orgIds = pageList.getRecords().stream().map(OrgMember::getOrgId).collect(Collectors.toSet());
        Map<Long, Org> orgMap = this.orgService.findIdsMap(orgIds);

        return pageList.convert(item -> {
            OrgMemberDto orgMemberDto = new OrgMemberDto()
                .setId(item.getId())
                .setName(item.getName())
                .setOrgId(item.getOrgId())
                .setOrgName(orgMap.containsKey(item.getOrgId()) ? orgMap.get(item.getOrgId()).getName() : null)
                .setAvatar(item.getAvatar())
                .setGender(item.getGender())
                .setGenderName(GenderEnum.findByCode(item.getGender()).getName())
                .setMobile(item.getMobile())
                .setWorkNum(item.getWorkNum());
            return orgMemberDto;
        });
    }

    public Long save(OrgMemberReq req){
        OrgMember orgMember = req.getId() != null ? orgMemberMapper.selectById(req.getId()) : new OrgMember();
        String oldAvatar = orgMember.getAvatar();
        orgMember.setName(req.getName())
            .setAvatar(req.getAvatar() != null ? FileUtil.uploadFile(req.getAvatar()) : null )
            .setOrgId(req.getOrgId())
            .setGender(req.getGender())
            .setMobile(req.getMobile())
            .setWorkNum(StringUtils.isNotBlank(req.getWorkNum()) ? req.getWorkNum() : RandomStringUtils.randomAlphanumeric(8))
            .setId(req.getId());
        if(StringUtils.isBlank(orgMember.getPwd())){
            orgMember.setPwd(passwordEncoder.encode(orgMember.getWorkNum()));
        }
        validWorkNum(orgMember.getWorkNum(), req.getId());
        super.saveOrUpdate(orgMember);

        if(req.getAvatar() != null){
            FileUtil.deleteFile(oldAvatar);
        }
        return orgMember.getId();

    }

    /**
     * 删除
     * @param ids
     */
    public void deleted(Collection<Long> ids){
        if(CollectionUtils.isEmpty(ids)){
            throw new BizException("", "请选择要删除的数据");
        }
        this.orgMemberMapper.deleteBatchIds(ids);
        this.sysMemberRoleService.deletedByMemberId(ids);
    }

    /**
     * 验证工号是否使用
     */
    public void validWorkNum(String workNum, Long id){
        QueryWrapper<OrgMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrgMember::getWorkNum, workNum);
        queryWrapper.lambda().ne(id != null, OrgMember::getId, id);
        int count = this.orgMemberMapper.selectCount(queryWrapper);
        if(count > 0) {
            throw new BizException("", "工号已被使用");
        }
    }

    /**
     * 搜索人员信息
     * @param key
     * @param notMemberIds
     * @return
     */
    public List<OrgMember> searchUser(String key, Collection<Long> notMemberIds){
        QueryWrapper<OrgMember> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(key)){
            queryWrapper.lambda().and(wrapper -> wrapper.like(OrgMember::getName, key)
                .or().like(OrgMember::getWorkNum, key).or().like(OrgMember::getMobile, key));
        }
        queryWrapper.lambda().notIn(!CollectionUtils.isEmpty(notMemberIds), OrgMember::getId, notMemberIds);
        queryWrapper.last("limit 100");
        return this.orgMemberMapper.selectList(queryWrapper);
    }

    /**
     * 搜索
     * @param key
     * @param memberIds
     * @return
     */
    public List<OrgMember> searchUserByIds(String key, Collection<Long> memberIds){
        if(CollectionUtils.isEmpty(memberIds)){
            return new ArrayList<>();
        }
        QueryWrapper<OrgMember> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(key)){
            queryWrapper.lambda().and(wrapper -> wrapper.like(OrgMember::getName, key)
                    .or().like(OrgMember::getWorkNum, key).or().like(OrgMember::getMobile, key));
        }
        queryWrapper.lambda().in(OrgMember::getId, memberIds);
        queryWrapper.last("limit 100");
        return this.orgMemberMapper.selectList(queryWrapper);
    }
}
