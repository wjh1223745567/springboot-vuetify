package com.iotinall.framework.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 不带软删除基础类
 */

@Data
@Accessors(chain = true)
public class BaseCreateEntity {

    @IsKey
    @Index
    private Long id; // 主键

    /**
     * @see
     */
    @Column
    @ColumnComment(value = "创建人")
    private Long createId;

    /**
     * 是否显示
     */
    @Column
    @ColumnComment("显示状态")
    @DefaultValue("false")
    private Boolean deleted;

    @Column
    @DefaultValue("CURRENT_TIMESTAMP")
    @ColumnComment("创建时间")
    @IgnoreUpdate
    private LocalDateTime createTime;

    @Column
    @TableField(exist = false)
    @DefaultValue("CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @ColumnComment("更新时间")
    private LocalDateTime updateTime;

    @Column(length = 1000)
    private String remark;
}
