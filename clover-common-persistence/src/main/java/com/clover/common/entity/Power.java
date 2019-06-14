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
 * 权限表
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
@TableName("Power")
public class Power extends Model<Power> {

    private static final long serialVersionUID = 1L;

    /**
     * 权限id
     */
    @TableId(value="power_id",type= IdType.AUTO)
    private String power_id;

    /**
     * 权限描述
     */
    @TableField("detail")
    private String detail;

    /**
     * 权限启用
     */
    @TableField("used")
    private String used;

    @Override
    protected Serializable pkVal() {
        return this.power_id;
    }

}
