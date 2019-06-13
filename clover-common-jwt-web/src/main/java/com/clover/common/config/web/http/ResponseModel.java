package com.clover.common.config.web.http;

/**
 * 统一返回相应参数实体类
 *
 * @author liugh 53182347@qq.com
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class ResponseModel<T> implements Serializable {
  private static final long serialVersionUID = -1241360949457314497L;
  public static final ResponseModel FAIL = new ResponseModel("失败", 500);
  public static final ResponseModel SUCCESS = new ResponseModel("成功", 200);
  private T data;
  private String msg;
  private Integer code;

  public ResponseModel() {
    HttpServletResponse response =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    response.setCharacterEncoding("UTF-8");
  }

  public ResponseModel(String msg, Integer code) {
    this.msg = msg;
    this.code = code;
  }
}
