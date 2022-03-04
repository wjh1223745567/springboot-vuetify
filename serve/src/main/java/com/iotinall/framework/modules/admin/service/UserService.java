package com.iotinall.framework.modules.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iotinall.framework.common.exception.BizException;
import com.iotinall.framework.constants.enums.GenderEnum;
import com.iotinall.framework.modules.admin.dto.orgmember.OrgMemberPwdReq;
import com.iotinall.framework.modules.admin.entity.Org;
import com.iotinall.framework.modules.admin.entity.OrgMember;
import com.iotinall.framework.modules.admin.mapper.OrgMapper;
import com.iotinall.framework.modules.admin.mapper.OrgMemberMapper;
import com.iotinall.framework.modules.security.module.SecurityUserModel;
import com.iotinall.framework.modules.security.util.SecurityUtil;
import com.iotinall.framework.util.AesEncryptUtils;
import com.iotinall.framework.util.CodeOrderUtil;
import com.iotinall.framework.util.ValidateCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author WJH
 * @date 2019/12/1618:22
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    @Resource
    private OrgMemberMapper orgMemberMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private OrgMapper orgMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private SysRoleService sysRoleService;

    private static final String adminUser = "admin";

    private static final String errorMsg = "用户名或密码错误";

    @Value("${tokenTimeOut}")
    private Integer tokenTimeOut;

    public SecurityUserModel login(String username, String password) {
        QueryWrapper<OrgMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrgMember::getWorkNum, username);
        OrgMember orgMember = Objects.equals(adminUser, username) ? orgMemberMapper.findAllByWorkNum(adminUser) : orgMemberMapper.selectOne(queryWrapper);

        if(orgMember == null){
            throw new BizException("", errorMsg);
        }

        if(!passwordEncoder.matches(password, orgMember.getPwd())){
            throw new BizException("", errorMsg);
        }

        Org org = this.orgMapper.selectById(orgMember.getOrgId());

        String token = CodeOrderUtil.token(username);
        SecurityUserModel securityUserModel = new SecurityUserModel()
                .setUserName(username)
                .setId(orgMember.getId())
                .setName(orgMember.getName())
                .setAvatar(orgMember.getAvatar())
                .setMobile(orgMember.getMobile())
                .setToken(token)
                .setOrgId(orgMember.getOrgId())
                .setOrgName(org != null ? org.getName() : null)
                .setAuthorizes(Objects.equals(username, adminUser) ? Collections.singletonList("ADMIN") : this.sysRoleService.getPermission(orgMember.getId()));
        if(CollectionUtils.isEmpty(securityUserModel.getAuthorizes())){
            throw new BizException("", "未授权用户");
        }
        redisTemplate.opsForValue().set(token, securityUserModel, tokenTimeOut, TimeUnit.MINUTES);
        return securityUserModel;
    }

    public void updatePwd(OrgMemberPwdReq req){
        OrgMember orgMember = Objects.equals(adminUser, SecurityUtil.getCurrUserName()) ? this.orgMemberMapper.findAllByWorkNum(adminUser) : this.orgMemberMapper.selectById(SecurityUtil.getCurrUserID());
        if(orgMember == null){
            throw new BizException("", "未找到当前用户");
        }
        String old = AesEncryptUtils.aesDecrypt(req.getOldpwd());
        String newPwd = AesEncryptUtils.aesDecrypt(req.getNewpwd());
        if(!passwordEncoder.matches(old, orgMember.getPwd())){
            throw new BizException("", "旧密码错误");
        }
        Integer count = this.orgMemberMapper.updatePwd(passwordEncoder.encode(newPwd), SecurityUtil.getCurrUserID());
        if(count == 0){
            throw new BizException("", "密码修改失败");
        }
    }

    /**
     * 登出
     */
    public void loginOut() {
        redisTemplate.delete(SecurityUtil.getCurrUserModel().getToken());
    }

    public Map<String, String> getRandomCodeImage() {
        Map<String, String> result = ValidateCodeUtil.getRandomCodeImage();
        String validCode = UUID.randomUUID().toString().replace("-", "");
        if (result != null) {
            String value = result.get("key");
            redisTemplate.opsForValue().set(validCode, value, 180, TimeUnit.SECONDS);
        }
        result.remove("key");
        result.put("valueKey", validCode);
        return result;
    }


    /**
     * 初始化超级管理员
     */
    public void initSupperUser(){
        OrgMember orgMember = this.orgMemberMapper.findAllByWorkNum(adminUser);
        if(orgMember == null){
            orgMember = new OrgMember();
            orgMember.setWorkNum(adminUser)
                    .setName("超级管理员")
                    .setGender(GenderEnum.MAN.getCode())
                    .setOrgId(0L)
                    .setPwd(passwordEncoder.encode("1234567"))
                    .setDeleted(Boolean.TRUE);
            this.orgMemberMapper.insert(orgMember);
        }
    }
}
