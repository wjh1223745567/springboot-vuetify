package com.iotinall.framework.modules.admin.dto.orgmember;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author WJH
 * @date 2019/12/2013:41
 */
@Data
@Accessors(chain = true)
public class OrgMemberDto implements Serializable {

    private Long id;
    /**
     * 头像
     */
    private String avatar;

    private String name; // 姓名

    private String workNum; // 工号

    private Integer gender;

    private String genderName;

    private String mobile; // 手机号码

    private Long orgId;

    private String orgName;

    private List<Role> roles;

    @Data
    @Accessors(chain = true)
    public class Role implements Serializable{

        private Long id;

        private String name;

    }

}
