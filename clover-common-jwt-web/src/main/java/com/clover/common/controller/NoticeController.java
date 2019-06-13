package com.clover.common.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.clover.common.annotation.CurrentUser;
import com.clover.common.annotation.ValidationParam;
import com.clover.common.base.PublicResultConstant;
import com.clover.common.config.web.http.ResponseHelper;
import com.clover.common.config.web.http.ResponseModel;
import com.clover.common.entity.Admin;
import com.clover.common.entity.Notice;
import com.clover.common.service.INoticeService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * <p>
 * 消息通知表 前端控制器
 * </p>
 *
 * @author liugh
 * @since 2019-05-09
 */
@RestController
@RequestMapping("/notice")
@ApiIgnore
public class NoticeController {

    @Resource
    private INoticeService noticeService;

    /**
     * 获取自己的消息列表
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @GetMapping("/infoList")
    public ResponseModel<Page<Notice>> findInfoList(@CurrentUser Admin admin, @RequestParam(name = "pageIndex", defaultValue = "1", required = false) Integer pageIndex,
                                                    @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) throws Exception{

        return ResponseHelper.buildResponseModel(noticeService.selectPage(new Page<>(pageIndex, pageSize),new EntityWrapper<Notice>().
                eq("mobile", admin.getMobile()).orderBy("create_time",false)));
    }

    /**
     * 删除全部消息
     * @return
     * @throws Exception
     */
    @DeleteMapping
    public ResponseModel findInfoList(@CurrentUser Admin admin) throws Exception{
        noticeService.deleteByNotices(admin);
        return ResponseHelper.buildResponseModel(PublicResultConstant.SUCCEED);
    }

    /**
     * 消息改变为已读
     * @param requestJson
     * @return
     * @throws Exception
     */
    @PostMapping("/read")
    public ResponseModel read(@ValidationParam("noticeId,isRead")
                             @RequestBody JSONObject requestJson) throws Exception{
        noticeService.read(requestJson);
        return ResponseHelper.buildResponseModel(PublicResultConstant.SUCCEED);
    }
    /**
     * 未读消息总数
     * @return
     * @throws Exception
     */
    @GetMapping("/noReadCount")
    public ResponseModel getNoRead(@CurrentUser Admin admin) throws Exception{
        return ResponseHelper.buildResponseModel(noticeService.selectList(new
                EntityWrapper<Notice>().where("mobile = {0} and is_read = 0", admin.getMobile())).size());
    }


}

