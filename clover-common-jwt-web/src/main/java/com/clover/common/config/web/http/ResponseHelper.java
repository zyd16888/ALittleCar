package com.clover.common.config.web.http;

import com.clover.common.base.PublicResultConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * 统一返回相应参数
 * @author liugh 53182347@qq.com
 */
public class ResponseHelper {

    public ResponseHelper() {
    }

    public static <T> ResponseModel<T> notFound(String message) {
        ResponseModel response = new ResponseModel();
        response.setCode(HttpStatus.NOT_FOUND.value());
        response.setMsg(message);
        return response;
    }

    public static <T> ResponseModel<T> internalServerError(String message) {
        ResponseModel response = new ResponseModel();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMsg(message);
        return response;
    }

    public static <T> ResponseModel<T> validationFailure(String message) {
    if (StringUtils.isNoneBlank(message)&&message.equalsIgnoreCase(PublicResultConstant.UNAUTHORIZED)){
       return new ResponseModel(message, 1001);
    }
      return new ResponseModel(message, HttpStatus.BAD_REQUEST.value());
    }

    public static <T> ResponseModel<T> buildResponseModel(T result) {
        ResponseModel response = new ResponseModel();
        response.setCode(HttpStatus.OK.value());
        response.setMsg(HttpStatus.OK.getReasonPhrase());
        response.setData(result);
        return response;
    }
}
