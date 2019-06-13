package com.clover.common.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.clover.common.entity.Admin;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    Admin getUserByUserName(String username);

    Admin getUserByMobile(String mobile);

    /**
     * 注册用户
     * @param admin
     * @param roleCode
     * @return
     */
    Admin register(Admin admin, String roleCode);

    Map<String, Object> getLoginUserAndMenuInfo(Admin admin);

    void deleteByUserNo(String userNo)throws Exception;

    Page<Admin> selectPageByConditionUser(Page<Admin> userPage, String info, Integer[] status, String startTime, String endTime);

    Map<String,Object> checkUsernameAndPassword(JSONObject requestJson) throws Exception;

    Map<String,Object> checkMobileAndPasswd(JSONObject requestJson)throws Exception;

    Map<String,Object> checkMobileAndCatcha(JSONObject requestJson)throws Exception;

    Admin checkAndRegisterUser(JSONObject requestJson)throws Exception;

    Admin updateForgetPasswd(JSONObject requestJson)throws Exception;

    void resetMobile(Admin currentAdmin, JSONObject requestJson)throws Exception;

    void resetPassWord(Admin currentAdmin, JSONObject requestJson)throws Exception;

}
