package com.iotinall.framework.modules.admin.dto.org;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 组织部门
 * @author WJH
 * @date 2019/12/1310:10
 */
@Data
@Accessors(chain = true)
public class OrgReq {

    private Long id;

    /**
     * 名称
     */
    @NotBlank
    @Length(max = 64)
    private String name;


    /**
     * 父ID
     */
    private Long pid;

    /**
     * 备注
     */
    @Length(max = 1024)
    private String remark;

}
