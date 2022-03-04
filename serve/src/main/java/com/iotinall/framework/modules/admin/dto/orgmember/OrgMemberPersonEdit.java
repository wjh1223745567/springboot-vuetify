package com.iotinall.framework.modules.admin.dto.orgmember;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * 自己修改用户
 * @author WJH
 * @date 2020/1/316:54
 */
@Data
public class OrgMemberPersonEdit {

    private Long id;

    private MultipartFile avatar;

    @Length(max = 32)
    private String name; // 姓名

    @Length(max = 15)
    private String mobile; // 手机号码

}
