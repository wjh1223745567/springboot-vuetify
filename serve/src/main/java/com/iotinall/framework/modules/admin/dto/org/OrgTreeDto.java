package com.iotinall.framework.modules.admin.dto.org;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WJH
 * @date 2019/10/2816:18
 */
@Data
@Accessors(chain = true)
public class OrgTreeDto implements Serializable {

    private Long id;

    private String name;

    private Long pid;

    private String remark;

    private List<OrgTreeDto> children =  new ArrayList<>();
}
