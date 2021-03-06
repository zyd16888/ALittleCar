package com.clover.common.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("roles")
public class Role extends Model<Role> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId(value="r_id",type= IdType.AUTO)
    private String r_id;

    /**
     * 权限id
     */
    @TableField("power_id")
    private String power_id;

    /**
     * 权限描述
     */
    @TableField("mark")
    private String mark;

    /**
     * 角色名
     */
    @TableField("name")
    private String name;

    @Override
    protected Serializable pkVal() {
        return this.r_id;
    }

}
