package com.iotinall.framework.modules.admin.controller;

import com.iotinall.framework.common.exception.BizException;
import com.iotinall.framework.common.model.ResultDTO;
import com.iotinall.framework.modules.admin.dto.orgmember.OrgMemberPwdReq;
import com.iotinall.framework.modules.admin.service.UserService;
import com.iotinall.framework.modules.security.module.SecurityUserModel;
import com.iotinall.framework.modules.security.util.SecurityUtil;
import com.iotinall.framework.util.AesEncryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author WJH
 * @date 2019/12/1618:21
 */
@Slf4j
@RestController
@RequestMapping(value = "user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 登陆接口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @PostMapping(value = "login")
    public ResultDTO login(@RequestParam String username, @RequestParam String password, @RequestParam String validCode, @RequestParam String valueKey){
        String trueValidCode = redisTemplate.opsForValue().get(valueKey);
        if(StringUtils.isBlank(trueValidCode)){
            throw new BizException("1", "验证码已过期！");
        }
        if(!validCode.equalsIgnoreCase(trueValidCode)){
            throw new BizException("1", "验证码错误请重新输入");
        }
        redisTemplate.delete(valueKey);
        return ResultDTO.success(this.userService.login(AesEncryptUtils.aesDecrypt(username), AesEncryptUtils.aesDecrypt(password)));
    }

    @GetMapping(value = "info")
    public ResultDTO userInfo(){
        SecurityUserModel model = SecurityUtil.getCurrUserDetails().getSecurityUserModel();
        return ResultDTO.success(model);
    }

    @GetMapping(value = "logout")
    public ResultDTO loginOut(){
        this.userService.loginOut();
        return ResultDTO.success();
    }

    /**
     * 获取验证码
     */
    @GetMapping(value = "valid_img")
    public ResultDTO validImg(){
        Map<String, String> res = this.userService.getRandomCodeImage();
        return ResultDTO.success(res);
    }

    /**
     * 更改密码
     * @param req
     * @return
     */
    @PostMapping(value = "update_pwd")
    public ResultDTO<?> updatePwd(@Valid OrgMemberPwdReq req){
        this.userService.updatePwd(req);
        return ResultDTO.success();
    }

}
