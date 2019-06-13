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
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ResponsePageModel<T> implements Serializable {
  private static final long serialVersionUID = -1241360949457314497L;
  private Integer code;
  private String msg;
  private Integer count;
  private List<T> data=new ArrayList<>(10);

  public ResponsePageModel() {
    HttpServletResponse response =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    response.setCharacterEncoding("UTF-8");
  }
}
