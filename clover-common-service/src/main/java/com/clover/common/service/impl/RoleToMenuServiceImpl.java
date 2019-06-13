package com.clover.common.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.clover.common.entity.RoleToMenu;
import com.clover.common.mapper.RoleToMenuMapper;
import com.clover.common.service.IRoleToMenuService;
import com.clover.common.util.ComUtil;
import org.springframework.stereotype.Service;

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
public class RoleToMenuServiceImpl extends ServiceImpl<RoleToMenuMapper, RoleToMenu> implements IRoleToMenuService {

    @Override
    //redis生成key注解，以类名方法名和参数组成key
//    @Cacheable(value = "AdminToRole",keyGenerator="wiselyKeyGenerator")
    public List<RoleToMenu> selectByRoleCode(String roleCode) {
        EntityWrapper<RoleToMenu> ew = new EntityWrapper<>();
        ew.where("role_code={0}", roleCode);
        return this.selectList(ew);
    }

    @Override
    public boolean saveAll(String roleCode, List<String> menuCodes) {
        boolean result = true;
        if (!ComUtil.isEmpty(menuCodes)) {
            List<RoleToMenu> modelList = new ArrayList<>();
            for (String menuCode : menuCodes) {
                modelList.add(RoleToMenu.builder().roleCode(roleCode).menuCode(menuCode).build());
            }
            result = this.insertBatch(modelList);
        }
        return result;
    }

    @Override
    public boolean deleteAllByRoleCode(String roleCode) {
        EntityWrapper<RoleToMenu> ew = new EntityWrapper<>();
        ew.where("role_code={0}", roleCode);
        return this.delete(ew);
    }
}
