package com.iotinall.framework.modules.admin.dto.org;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 部门适用范围
 * @author WJH
 * @date 2019/12/2419:39
 */
@Data
@Accessors(chain = true)
public class OrgCheckTreeDto {

    private Long id;

    private String name;

    private Boolean isorg;

    private String code;

    private String avatar;

    private String workNum;

    private Integer gender;

    private String orgName;

    private String phone;

    /**
     * 是否包含子节点
     */
    private Boolean haveChildren;

}
