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
 * 管理员列表
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
@TableName("Manager")
public class Manager extends Model<Manager> {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员id
     */
    @TableId(value="u_id",type = IdType.AUTO)
    private String u_id;

    /**
     * 角色id
     */
    @TableField("r_id")
    private String r_id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 创建时间
     */
    @TableField("jointime")
    private String jointime;

    /**
     * 状态
     */
    @TableField("state")
    private String state;

    /**
     * 备注
     */
    @TableField("mark")
    private String mark;

    @Override
    protected Serializable pkVal() {
        return this.u_id;
    }

}
