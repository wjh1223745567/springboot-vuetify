package com.iotinall.framework.modules.admin.dto.orgmember;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author WJH
 * @date 2019/12/1314:47
 */
@Data
public class OrgMemberReq {

    private Long id;

    private MultipartFile avatar;

    @Length(max = 64)
    @NotBlank
    private String name; // 姓名

    @NotNull
    @Max(2)
    @Min(0)
    private Integer gender;

    @Length(max = 30)
    private String workNum;

    @Length(max = 15)
    private String mobile; // 手机号码
    @NotNull
    private Long orgId;

    @Length(max = 1024)
    private String remark;

}
