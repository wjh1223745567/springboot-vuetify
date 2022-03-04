package com.iotinall.framework.modules.admin.dto.orgmember;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrgMemberSimpleDto {

    private Long id;

    private String name;

    private String orgName;

    private String mobile;

    private String avatar;

}
