package com.clover.common.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.clover.common.base.Constant;
import com.clover.common.entity.Admin;
import com.clover.common.entity.UserThirdparty;
import com.clover.common.mapper.UserThirdpartyMapper;
import com.clover.common.model.ThirdPartyUser;
import com.clover.common.service.IAdminService;
import com.clover.common.service.IUserThirdpartyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 第三方用户表 服务实现类
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
@Service
public class UserThirdpartyServiceImpl extends ServiceImpl<UserThirdpartyMapper, UserThirdparty> implements IUserThirdpartyService {

    @Resource
    private IAdminService adminService;

    @Override
    public Admin insertThirdPartyUser(ThirdPartyUser param, String password) throws Exception{
        Admin sysAdmin = Admin.builder().password(password).username("游客"+param.getOpenid()).mobile(param.getOpenid())
                .avatar(param.getAvatarUrl()).build();
        Admin register = adminService.register(sysAdmin, Constant.RoleType.USER);
        // 初始化第三方信息
        UserThirdparty thirdparty = UserThirdparty.builder().providerType(param.getProvider()).openId(param.getOpenid()).createTime(System.currentTimeMillis())
                .userNo(register.getUserNo()).status(Constant.ENABLE).accessToken(param.getToken()).build();
        this.insert(thirdparty);
        return register;
    }
}
