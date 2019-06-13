package com.clover.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.clover.common.base.BusinessException;
import com.clover.common.base.Constant;
import com.clover.common.base.PublicResultConstant;
import com.clover.common.entity.Admin;
import com.clover.common.entity.AdminToRole;
import com.clover.common.entity.Menu;
import com.clover.common.entity.SmsVerify;
import com.clover.common.mapper.AdminMapper;
import com.clover.common.service.*;
import com.clover.common.util.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

  @Resource private IAdminToRoleService adminToRoleService;
  @Resource private IMenuService menuService;

  @Resource private AdminMapper adminMapper;

  @Resource private INoticeService noticeService;

  @Resource private ISmsVerifyService smsVerifyService;

  @Resource private IRoleService roleService;

  @Override
  //    @Cacheable(value = "AdminToRole",keyGenerator="wiselyKeyGenerator")
  public Admin getUserByUserName(String username) {
    System.out.println("执行getUserByUserName方法了.....");
    EntityWrapper<Admin> ew = new EntityWrapper<>();
    ew.where("user_name={0}", username);
    return this.selectOne(ew);
  }

  @Override
  public Admin getUserByMobile(String mobile) {
    EntityWrapper<Admin> ew = new EntityWrapper<>();
    ew.eq("mobile", mobile);
    return this.selectOne(ew);
  }

  @Override
  public Admin register(Admin admin, String roleCode) {
    admin.setUserNo(GenerationSequenceUtil.generateUUID("admin"));
    admin.setCreateTime(System.currentTimeMillis());
    boolean result = this.insert(admin);
    if (result) {
      AdminToRole adminToRole =
          AdminToRole.builder().userNo(admin.getUserNo()).roleCode(roleCode).build();
      adminToRoleService.insert(adminToRole);
    }
    return admin;
  }

  @Override
  public Map<String, Object> getLoginUserAndMenuInfo(Admin admin) {
    Map<String, Object> result = new HashMap<>(5);
    AdminToRole adminToRole = adminToRoleService.selectByUserNo(admin.getUserNo());
    admin.setToken(JWTUtil.sign(admin.getUserNo(), admin.getPassword()));
    result.put("user", admin);
    // 根据角色主键查询启用的菜单权限
    List<Menu> buttonList = new ArrayList<Menu>();
    List<Menu> menuList = menuService.findMenuByRoleCode(adminToRole.getRoleCode());
    List<Menu> retMenuList = menuService.treeMenuList(Constant.ROOT_MENU, menuList);
    for (Menu buttonMenu : menuList) {
      if (buttonMenu.getMenuType() == Constant.TYPE_BUTTON) {
        buttonList.add(buttonMenu);
      }
    }
    result.put("menuList", retMenuList);
    result.put("buttonList", buttonList);
    return result;
  }

  @Override
  public void deleteByUserNo(String userNo) throws Exception {
    Admin admin = this.selectById(userNo);
    if (ComUtil.isEmpty(admin)) {
      throw new BusinessException(PublicResultConstant.INVALID_USER);
    }
    EntityWrapper<AdminToRole> ew = new EntityWrapper<>();
    ew.eq("user_no", userNo);
    adminToRoleService.delete(ew);
    this.deleteById(userNo);
  }

  @Override
  public Page<Admin> selectPageByConditionUser(
      Page<Admin> userPage, String info, Integer[] status, String startTime, String endTime) {
    // 注意！！ 分页 total 是经过插件自动 回写 到传入 page 对象
    return userPage.setRecords(
        adminMapper.selectPageByConditionUser(userPage, info, status, startTime, endTime));
  }

  @Override
  public Map<String, Object> checkUsernameAndPassword(JSONObject requestJson) throws Exception {
    // 由于 @ValidationParam注解已经验证过mobile和passWord参数，所以可以直接get使用没毛病。
    String username = requestJson.getString("username");

    Admin admin =
            this.selectOne(new EntityWrapper<Admin>().where("user_name = {0} and status = 1", username));
    if (ComUtil.isEmpty(admin)
            || !BCrypt.checkpw(requestJson.getString("password"), admin.getPassword())) {
      throw new BusinessException(PublicResultConstant.INVALID_USERNAME_PASSWORD);
    }

    admin.setRoleName(null);
    return this.getLoginUserAndMenuInfo(admin);
  }

  @Override
  public Map<String, Object> checkMobileAndPasswd(JSONObject requestJson) throws Exception {
    // 由于 @ValidationParam注解已经验证过mobile和passWord参数，所以可以直接get使用没毛病。
    String mobile = requestJson.getString("mobile");
    if (!StringUtil.checkMobileNumber(mobile)) {
      throw new BusinessException(PublicResultConstant.MOBILE_ERROR);
    }
    Admin admin =
        this.selectOne(new EntityWrapper<Admin>().where("mobile = {0} and status = 1", mobile));
    if (ComUtil.isEmpty(admin)
        || !BCrypt.checkpw(requestJson.getString("password"), admin.getPassword())) {
      throw new BusinessException(PublicResultConstant.INVALID_USERNAME_PASSWORD);
    }
    // 测试websocket用户登录给管理员发送消息的例子  前端代码参考父目录下WebSocketDemo.html
    //        noticeService.insertByThemeNo("themeNo-cwr3fsxf233edasdfcf2s3","13888888888");
    //        MyWebSocketService.sendMessageTo(JSONObject.toJSONString(admin),"13888888888");
    admin.setRoleName(null);
    return this.getLoginUserAndMenuInfo(admin);
  }

  @Override
  public Map<String, Object> checkMobileAndCatcha(JSONObject requestJson) throws Exception {
    String mobile = requestJson.getString("mobile");
    if (!StringUtil.checkMobileNumber(mobile)) {
      throw new BusinessException(PublicResultConstant.MOBILE_ERROR);
    }
    Admin admin = this.getUserByMobile(mobile);
    // 如果不是启用的状态
    if (!ComUtil.isEmpty(admin) && admin.getStatus() != Constant.ENABLE) {
      throw new BusinessException("该用户状态不是启用的!");
    }
    List<SmsVerify> smsVerifies =
        smsVerifyService.getByMobileAndCaptchaAndType(
            mobile,
            requestJson.getString("captcha"),
            SmsSendUtil.SMSType.getType(SmsSendUtil.SMSType.AUTH.name()));
    if (ComUtil.isEmpty(smsVerifies)) {
      throw new BusinessException(PublicResultConstant.VERIFY_PARAM_ERROR);
    }
    if (SmsSendUtil.isCaptchaPassTime(smsVerifies.get(0).getCreateTime())) {
      throw new BusinessException(PublicResultConstant.VERIFY_PARAM_PASS);
    }
    if (ComUtil.isEmpty(admin)) {
      // 设置默认密码
      Admin adminRegister =
          Admin.builder()
              .password(BCrypt.hashpw("123456", BCrypt.gensalt()))
              .mobile(mobile)
              .username(mobile)
              .build();
      admin = this.register(adminRegister, Constant.RoleType.USER);
    }
    return this.getLoginUserAndMenuInfo(admin);
  }


  @Override
  public Admin checkAndRegisterUser(JSONObject requestJson) throws Exception {
    // 可直接转为java对象,简化操作,不用再set一个个属性
    Admin adminRegister = requestJson.toJavaObject(Admin.class);
    if (!StringUtil.checkMobileNumber(adminRegister.getMobile())) {
      throw new BusinessException(PublicResultConstant.MOBILE_ERROR);
    }
    if (!adminRegister.getPassword().equals(requestJson.getString("rePassword"))) {
      throw new BusinessException(PublicResultConstant.INVALID_RE_PASSWORD);
    }
    List<SmsVerify> smsVerifies =
        smsVerifyService.getByMobileAndCaptchaAndType(
            adminRegister.getMobile(),
            requestJson.getString("captcha"),
            SmsSendUtil.SMSType.getType(SmsSendUtil.SMSType.REG.name()));
    if (ComUtil.isEmpty(smsVerifies)) {
      throw new BusinessException(PublicResultConstant.VERIFY_PARAM_ERROR);
    }
    // 验证码是否过期
    if (SmsSendUtil.isCaptchaPassTime(smsVerifies.get(0).getCreateTime())) {
      throw new BusinessException(PublicResultConstant.VERIFY_PARAM_PASS);
    }
    adminRegister.setPassword(BCrypt.hashpw(requestJson.getString("password"), BCrypt.gensalt()));
    // 默认注册普通用户
    return this.register(adminRegister, Constant.RoleType.USER);
  }

  @Override
  public Admin updateForgetPasswd(JSONObject requestJson) throws Exception {
    String mobile = requestJson.getString("mobile");
    if (!StringUtil.checkMobileNumber(mobile)) {
      throw new BusinessException(PublicResultConstant.MOBILE_ERROR);
    }
    if (!requestJson.getString("password").equals(requestJson.getString("rePassword"))) {
      throw new BusinessException(PublicResultConstant.INVALID_RE_PASSWORD);
    }
    Admin admin = this.getUserByMobile(mobile);
    roleService.getRoleIsAdminByUserNo(admin.getUserNo());
    if (ComUtil.isEmpty(admin)) {
      throw new BusinessException(PublicResultConstant.INVALID_USER);
    }
    List<SmsVerify> smsVerifies =
        smsVerifyService.getByMobileAndCaptchaAndType(
            mobile,
            requestJson.getString("captcha"),
            SmsSendUtil.SMSType.getType(SmsSendUtil.SMSType.FINDPASSWORD.name()));
    if (ComUtil.isEmpty(smsVerifies)) {
      throw new BusinessException(PublicResultConstant.VERIFY_PARAM_ERROR);
    }
    if (SmsSendUtil.isCaptchaPassTime(smsVerifies.get(0).getCreateTime())) {
      throw new BusinessException(PublicResultConstant.VERIFY_PARAM_PASS);
    }
    admin.setPassword(BCrypt.hashpw(requestJson.getString("password"), BCrypt.gensalt()));
    this.updateById(admin);
    return admin;
  }

  @Override
  public void resetMobile(Admin currentAdmin, JSONObject requestJson) throws Exception {
    String newMobile = requestJson.getString("newMobile");
    if (!StringUtil.checkMobileNumber(newMobile)) {
      throw new BusinessException(PublicResultConstant.MOBILE_ERROR);
    }
    List<SmsVerify> smsVerifies =
        smsVerifyService.getByMobileAndCaptchaAndType(
            newMobile,
            requestJson.getString("captcha"),
            SmsSendUtil.SMSType.getType(SmsSendUtil.SMSType.MODIFYINFO.name()));
    if (ComUtil.isEmpty(smsVerifies)) {
      throw new BusinessException(PublicResultConstant.VERIFY_PARAM_ERROR);
    }
    if (SmsSendUtil.isCaptchaPassTime(smsVerifies.get(0).getCreateTime())) {
      throw new BusinessException(PublicResultConstant.VERIFY_PARAM_PASS);
    }
    currentAdmin.setMobile(newMobile);
    this.updateById(currentAdmin);
  }

  @Override
  public void resetPassWord(Admin currentAdmin, JSONObject requestJson) throws Exception {
    if (!requestJson.getString("password").equals(requestJson.getString("rePassword"))) {
      throw new BusinessException(PublicResultConstant.INVALID_RE_PASSWORD);
    }
    if (!BCrypt.checkpw(requestJson.getString("oldPassword"), currentAdmin.getPassword())) {
      throw new BusinessException(PublicResultConstant.INVALID_USERNAME_PASSWORD);
    }
    currentAdmin.setPassword(BCrypt.hashpw(requestJson.getString("password"), BCrypt.gensalt()));
    this.updateById(currentAdmin);
  }
}
