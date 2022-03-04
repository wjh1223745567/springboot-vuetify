package com.iotinall.framework.modules.admin.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 组织表
 * 组织的根节点为学校，一个系统中只有一个根节点
 */

@Data
@Table
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Org extends BaseCreateEntity {

    @Index
    @Column(length = 64, isNull = false, comment = "组织名称")
    private String name; // 组织名称

    @Unique
    @Column(length = 64, isNull = false, comment = "编码")
    @IgnoreUpdate
    private String code; // 编码

    @Index
    @Column(isNull = false, length = 300, comment = "全编码")
    private String fullCode; // 全编码

    @Column(isNull = false, defaultValue = "0", comment = "成员数量")
    private Integer memberCount; // 成员数量

    @Column(comment = "部门层级")
    private Integer leaveRank; // 当前部门层级
    /**
     * @see Org
     */
    @Column(comment = "父级ID")
    private Long pid; // 父级id
}
