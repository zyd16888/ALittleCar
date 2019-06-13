package com.clover.common.controller;


import com.clover.common.annotation.Log;
import com.clover.common.annotation.Pass;
import com.clover.common.config.web.http.ResponseHelper;
import com.clover.common.config.web.http.ResponseModel;
import com.clover.common.entity.SmsVerify;
import com.clover.common.service.ISmsVerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 验证码发送记录 前端控制器
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
@RestController
@RequestMapping("/smsVerify")
@Api(value="短信模块")
public class SmsVerifyController {

    @Resource
    private ISmsVerifyService smsVerifyService;

    /**
     * smsType 有四种类型：REG/注册账号 FINDPASSWORD/修改密码 AUTH/登陆验证 MODIFYINFO/修改账号
     * @param smsType
     * @param mobile
     * @return
     * @throws Exception
     */
    @ApiOperation(value="获取验证码接口", notes="路径参数,不需要Authorization")
    @GetMapping("/{smsType}/{mobile}")
    @Pass
    @Log(action="getCaptcha",modelName= "Sms",description="获取短信验证码接口")
   public ResponseModel<SmsVerify> getCaptcha (@PathVariable String smsType, @PathVariable String mobile) throws Exception{
        return ResponseHelper.buildResponseModel(smsVerifyService.addAndGetMobileAndCaptcha(smsType,mobile));
   }



    @ApiOperation(value="验证码验证接口", notes="请求参数,不需要Authorization")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "smsType", value = "验证码类型"
                    , required = true, dataType = "String",paramType="query"),
            @ApiImplicitParam(name = "mobile", value = "手机号"
                    , required = true, dataType = "String",paramType="query"),
            @ApiImplicitParam(name = "captcha", value = "验证码"
                    , required = true, dataType = "String",paramType="query")
    })
    @GetMapping("/captcha/check")
    @Pass
    public ResponseModel captchaCheck (@RequestParam String smsType,
            @RequestParam String mobile ,@RequestParam String captcha) throws Exception{
        smsVerifyService.captchaCheck(mobile,smsType,captcha);
        return ResponseHelper.buildResponseModel(true);
    }
}

