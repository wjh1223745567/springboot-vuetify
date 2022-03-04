package com.iotinall.framework.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Index;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.annotation.Unique;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Table
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class OrgMember extends BaseDeletedCreateEntity{

    @Column(length = 300, comment = "头像")
    private String avatar;

    @Index
    @Column(isNull = false, length = 64, comment = "姓名")
    private String name;

    @Index
    @Unique
    @Column(isNull = false, length = 70, comment = "工号")
    private String workNum;

    @Column(isNull = false, comment = "性别：0-女，1-男，2-未知")
    private Integer gender;

    @Index
    @Column(length = 20, comment = "电话")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String mobile;

    @Column(isNull = false, length = 300, comment = "密码")
    private String pwd;

    @Column(isNull = false, comment = "部门ID")
    private Long orgId;

}
