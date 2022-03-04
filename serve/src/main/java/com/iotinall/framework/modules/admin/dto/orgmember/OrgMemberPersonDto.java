package com.iotinall.framework.modules.admin.dto.orgmember;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * web客户端用户信息
 * @author WJH
 * @date 2020/1/720:08
 */
@Data
@Accessors(chain = true)
public class OrgMemberPersonDto {

    private Long id;

    private String avatar;

    private String name; // 姓名

    private String address;

    private String cardNum;

    private String mobile; // 手机号码

    private Long orgId;

    private String orgName;

    private List<String> roles;

}
