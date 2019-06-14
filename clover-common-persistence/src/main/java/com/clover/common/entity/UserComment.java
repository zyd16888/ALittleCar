package com.clover.common.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * 描述:
 * 用户评论实体类
 *
 * @author thaddeus
 * @date 2019-06-14 2:52 PM
 */

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("UserComment")
public class UserComment extends Model<UserComment>{
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID作为 主键，不能为空，自增
     */
    @TableId(value = "ID",type = IDType,AUTO)
    private String ID;
    /**
     * 用户id作为外键
     */
    private String m_id;
    /**
     * 评论内容
     */
    @TableField("C_Comments")
    private String C_Comments;
    /**
     * 评论跳转网址
     */
    @TableField("C_Html")
    private String C_Html;

    /**
     * 评论时间
     */
    @TableField("C_Time")
    private String C_Time;


    @Override
    protected Serializable pkVal() {
        return this.ID;
    }

    public static void main(String[] args) {
//    HMac md5Digest=new HMac();
//    md5Digest.update(Byte.parseByte(("hello"+"123")));
//    System.out.println(md5Digest.);
    }
}
