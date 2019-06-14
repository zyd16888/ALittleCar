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
 * 操作日志表
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
@TableName("Diary")
public class Diary extends Model<Diary> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private Integer id;
    /**
     * 日志内容
     */
    @TableField("content")
    private String content;
    /**
     * 类型
     */
    @TableField("type")
    private String type;
    /**
     * 用户名
     */
    @TableField("admin")
    private String admin;
    /**
     * 客户端的IP
     */
    @TableField("IP")
    private String IP;
    /**
     * 登录的时间
     */
    @TableField("Time")
    private String Time;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
