package com.clover.common.config.web;

import com.clover.common.annotation.Pass;
import com.clover.common.base.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liugh
 * @since 2019-05-09
 */
@Component
@Slf4j
public class PassUrlScanner implements CommandLineRunner {
  @Resource private RequestMappingHandlerMapping requestMappingHandlerMapping;

  @Value("${controller.scanPackage}")
  private String scanPackage;

  @Value("${server.context-path}")
  private String contextPath;

  @Override
  public void run(String... args)  {
    try {
      Constant.METHOD_URL_SET = urls();

      log.info("@Pass:" + Constant.METHOD_URL_SET);
      log.info("@Pass size:" + Constant.METHOD_URL_SET.size());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Set<Constant.URLInfo> urls() {
    List<Constant.URLInfo> urlList = new ArrayList<>();

    Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
    for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
      Constant.URLInfo urlInfo = new Constant.URLInfo();
      RequestMappingInfo info = m.getKey();
      HandlerMethod method = m.getValue();
      if (method.getMethodAnnotation(Pass.class) != null) {
        PatternsRequestCondition p = info.getPatternsCondition();
        for (String url : p.getPatterns()) {
          urlInfo.setUrl(contextPath + url);
        }
        urlInfo.setClassName(method.getMethod().getDeclaringClass().getName());
        urlInfo.setPass(method.getMethodAnnotation(Pass.class) != null);
        urlInfo.setMethod(method.getMethod().getName());
        RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
        String type = methodsCondition.toString();
        if (type != null && type.startsWith("[") && type.endsWith("]")) {
          type = type.substring(1, type.length() - 1);
          if (StringUtils.isBlank(type)) {
            if (method.getMethodAnnotation(RequestMapping.class) == null) {
              continue;
            }
          }
          urlInfo.setMethodType(
              StringUtils.isNoneBlank(type) ? type : RequestMapping.class.getName());
        }
        urlList.add(urlInfo);
      }
    }

    return new HashSet<>(urlList);
  }
}
