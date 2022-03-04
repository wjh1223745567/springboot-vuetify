package com.iotinall.framework.modules.admin.dto.org;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author WJH
 * @date 2019/12/1310:10
 */
@Data
public class OrgEdit {

    @NotNull
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
