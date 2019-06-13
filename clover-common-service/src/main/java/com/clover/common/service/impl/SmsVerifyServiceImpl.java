package com.clover.common.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.clover.common.base.BusinessException;
import com.clover.common.base.PublicResultConstant;
import com.clover.common.base.SmsSendResponse;
import com.clover.common.entity.SmsVerify;
import com.clover.common.mapper.SmsVerifyMapper;
import com.clover.common.service.ISmsVerifyService;
import com.clover.common.util.ComUtil;
import com.clover.common.util.GenerationSequenceUtil;
import com.clover.common.util.SmsSendUtil;
import com.clover.common.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 验证码发送记录 服务实现类
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
@Service
public class SmsVerifyServiceImpl extends ServiceImpl<SmsVerifyMapper, SmsVerify> implements ISmsVerifyService {

    @Override
    public List<SmsVerify> getByMobileAndCaptchaAndType(String mobile, String captcha, int type) {
        EntityWrapper<SmsVerify> smsQuery = new EntityWrapper<>();
        smsQuery.where("mobile={0} and sms_verify={1} and  sms_type = {2}",
                mobile,captcha,type);
        smsQuery.orderBy("create_time",false);
        return this.selectList(smsQuery);
    }

    @Override
    public SmsVerify addAndGetMobileAndCaptcha(String smsType, String mobile) throws Exception {
        if(!StringUtil.checkMobileNumber(mobile)){
            throw new BusinessException(PublicResultConstant.MOBILE_ERROR);
        }
        String randNum = GenerationSequenceUtil.getRandNum(4);
        SmsSendResponse smsSendResponse = SmsSendUtil.sendMessage(mobile,
                "校验码: " + randNum+"。您正在进行"+SmsSendUtil.SMSType.getSMSType(smsType).toString()+"的操作,请在5分钟内完成验证，注意保密哦！");
        SmsVerify smsVerify =SmsVerify.builder().smsId(smsSendResponse.getMsgId()).mobile(mobile).smsVerify(randNum)
                .smsType(SmsSendUtil.SMSType.getType(smsType)).createTime(System.currentTimeMillis()).build();
        this.insert(smsVerify);
        return smsVerify;
    }

    @Override
    public void captchaCheck(String mobile,String smsType, String captcha) throws Exception {
        if(!StringUtil.checkMobileNumber(mobile)){
            throw new BusinessException(PublicResultConstant.MOBILE_ERROR);
        }
        List<SmsVerify> smsVerifies = this.getByMobileAndCaptchaAndType(mobile,
                captcha,SmsSendUtil.SMSType.getType(smsType));
        if(ComUtil.isEmpty(smsVerifies)){
            throw new BusinessException(PublicResultConstant.VERIFY_PARAM_ERROR);
        }
        if(SmsSendUtil.isCaptchaPassTime(smsVerifies.get(0).getCreateTime())){
            throw new BusinessException(PublicResultConstant.VERIFY_PARAM_PASS);
        }
    }
}
