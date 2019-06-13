package com.clover.common.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.clover.common.entity.Admin;
import com.clover.common.entity.Notice;

/**
 * <p>
 * 消息通知表 服务类
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
public interface INoticeService extends IService<Notice> {

    /**
     *
     * @param themeNo 主题的主键,自己根据业务建立表赋值
     * @param mobile 电话
     */
    void insertByThemeNo(String themeNo,String  mobile);

    void deleteByNotices(Admin admin)throws Exception;

    void read(JSONObject requestJson)throws Exception;
}
