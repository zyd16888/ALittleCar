package com.clover.common.shiro;

import com.clover.common.annotation.CurrentUser;
import com.clover.common.entity.Admin;
import com.clover.common.exception.UnauthorizedException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 *  增加方法注入，将含有 @CurrentUser 注解的方法参数注入当前登录用户
 * @author liugh
 * @since 2019-05-09
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Admin.class)
                && parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Admin admin = (Admin) webRequest.getAttribute("currentUser", RequestAttributes.SCOPE_REQUEST);
        if (admin == null) {
            throw new UnauthorizedException("获取用户信息失败");
        }
        return admin;
    }
}
