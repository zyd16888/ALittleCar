package com.clover.common.shiro;

import com.clover.common.base.Constant;
import com.clover.common.entity.Admin;
import com.clover.common.entity.AdminToRole;
import com.clover.common.entity.Menu;
import com.clover.common.exception.UnauthorizedException;
import com.clover.common.service.*;
import com.clover.common.util.ComUtil;
import com.clover.common.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liugh
 * @since 2019-05-09
 */
public class MyRealm extends AuthorizingRealm {
    private IAdminService adminService;
    private IAdminToRoleService adminToRoleService;
    private IMenuService menuService;
    private IRoleService roleService;
    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (adminToRoleService == null) {
            this.adminToRoleService = SpringContextBeanService.getBean(IAdminToRoleService.class);
        }
        if (menuService == null) {
            this.menuService = SpringContextBeanService.getBean(IMenuService.class);
        }
        if (roleService == null) {
            this.roleService = SpringContextBeanService.getBean(IRoleService.class);
        }

        String userNo = JWTUtil.getUserNo(principals.toString());
        Admin admin = adminService.selectById(userNo);
        AdminToRole adminToRole = adminToRoleService.selectByUserNo(admin.getUserNo());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        /*
        Role role = roleService.selectOne(new EntityWrapper<Role>().eq("role_code", adminToRole.getRoleCode()));
        //添加控制角色级别的权限
        Set<String> roleNameSet = new HashSet<>();
        roleNameSet.add(role.getRoleName());
        simpleAuthorizationInfo.addRoles(roleNameSet);
        */
        //控制菜单级别按钮  类中用@RequiresPermissions("admin:list") 对应数据库中code字段来控制controller
        ArrayList<String> pers = new ArrayList<>();
        List<Menu> menuList = menuService.findMenuByRoleCode(adminToRole.getRoleCode());
        for (Menu per : menuList) {
             if (!ComUtil.isEmpty(per.getCode())) {
                  pers.add(String.valueOf(per.getCode()));
              }
        }
        Set<String> permission = new HashSet<>(pers);
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws UnauthorizedException {
        if (adminService == null) {
            this.adminService = SpringContextBeanService.getBean(IAdminService.class);
        }
        String token = (String) auth.getCredentials();
        if(Constant.isPass){
            return new SimpleAuthenticationInfo(token, token, this.getName());
        }
        // 解密获得username，用于和数据库进行对比
        String userNo = JWTUtil.getUserNo(token);
        if (userNo == null) {
            throw new UnauthorizedException("token invalid");
        }
        Admin adminBean = adminService.selectById(userNo);
        if (adminBean == null) {
            throw new UnauthorizedException("Admin didn't existed!");
        }
        if (! JWTUtil.verify(token, userNo, adminBean.getPassword())) {
            throw new UnauthorizedException("Username or password error");
        }
        return new SimpleAuthenticationInfo(token, token, this.getName());
    }
}
