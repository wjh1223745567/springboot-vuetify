package com.iotinall.framework.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 带软删除基础类
 */

@Data
@Accessors(chain = true)
public class BaseDeletedCreateEntity {

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
     * 删除状态
     */
    @Column
    @ColumnComment("显示状态")
    @DefaultValue("false")
    @TableLogic
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
