package com.iotinall.framework.modules.admin.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Index;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 系统角色
 * @author bingo
 * @date 12/10/2019 16:14
 */

@Data
@Table
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseCreateEntity{

    @Index
    @Column(isNull = false, length = 64, comment = "角色名称")
    private String name; // 角色名称

}
