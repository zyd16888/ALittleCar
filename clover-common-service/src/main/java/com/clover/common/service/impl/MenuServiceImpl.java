package com.clover.common.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.clover.common.base.Constant;
import com.clover.common.entity.Menu;
import com.clover.common.mapper.MenuMapper;
import com.clover.common.service.IMenuService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    //redis方法级别的缓存，需要做缓存打开改注解即可
    @Cacheable(value = "UserToRole",keyGenerator="wiselyKeyGenerator")
    public List<Menu> selectByIds(List<Integer> permissionIds) {
        EntityWrapper<Menu> ew = new EntityWrapper<>();
        ew.in("menu_id", permissionIds);
        return this.selectList(ew);
    }

    @Override
    public List<Menu> findMenuByRoleCode(String roleId) {
        return menuMapper.findMenuByRoleCode(roleId);
    }

    @Override
    public  List<Menu> treeMenuList(String pId, List<Menu> list) {
        List<Menu> menuArrayList = new ArrayList<>();
        for (Menu m : list) {
            if (String.valueOf(m.getParentId()).equals(pId)) {
                List<Menu> childMenuList = treeMenuList(String.valueOf(m.getMenuId()), list);
                m.setChildMenu(childMenuList);
                if(m.getMenuType() == Constant.TYPE_MENU){
                    menuArrayList.add(m);
                }
            }
        }
        return menuArrayList;
    }


}
