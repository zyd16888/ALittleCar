package com.clover.common.service;

import com.baomidou.mybatisplus.service.IService;
import com.clover.common.entity.Admin;
import com.clover.common.entity.UserThirdparty;
import com.clover.common.model.ThirdPartyUser;

/**
 * <p>
 * 第三方用户表 服务类
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
public interface IUserThirdpartyService extends IService<UserThirdparty> {

    Admin insertThirdPartyUser(ThirdPartyUser param, String password)throws Exception;

}
