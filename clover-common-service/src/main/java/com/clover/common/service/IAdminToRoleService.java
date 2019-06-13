package com.clover.common.service;

import com.baomidou.mybatisplus.service.IService;
import com.clover.common.entity.AdminToRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
public interface IAdminToRoleService extends IService<AdminToRole> {

    /**
     * 根据用户ID查询人员角色
     * @param userNo 用户ID
     * @return  结果
     */
    AdminToRole selectByUserNo(String  userNo);

}
