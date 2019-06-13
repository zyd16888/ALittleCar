package com.clover.common.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单表
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
@TableName("sdk_back_menu")
public class Menu extends Model<Menu> {

    private static final long serialVersionUID = 1L;
    /**
     * 菜单代号,规范权限标识
     */
    @TableId("menu_code")
    private String menuCode;
    /**
     * 父菜单主键
     */
    @TableField("parent_id")
    private Integer parentId;

    @TableField("menu_id")
    private Integer menuId;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 菜单类型，1：菜单  2：业务按钮
     */
    @TableField("menu_type")
    private Integer menuType;
    /**
     * 菜单的序号
     */
    private Integer num;
    /**
     * 菜单地址
     */
    private String url;

    private String code;

    private String icon;

    @TableField(exist = false)
    private List<Menu> childMenu;

    @Override
    protected Serializable pkVal() {
        return this.menuId;
    }

}
