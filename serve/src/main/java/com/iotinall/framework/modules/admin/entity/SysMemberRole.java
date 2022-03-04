package com.iotinall.framework.modules.admin.entity;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.annotation.Unique;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author bingo
 * @date 12/10/2019 19:57
 */
@Data
@Table
@Accessors(chain = true)
public class SysMemberRole implements Serializable {

    @IsKey
    private Long id;
    /**
     * 用户id
     * @see OrgMember
     */
    @Column
    @Unique(columns = {"member_id", "role_id"})
    private Long memberId;

    /**
     * @see SysRole 角色id
     */
    @Column
    private Long roleId;
}
