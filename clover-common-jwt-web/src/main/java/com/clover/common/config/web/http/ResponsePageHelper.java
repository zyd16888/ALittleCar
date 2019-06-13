package com.clover.common.config.web.http;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.http.HttpStatus;

/**
 * 统一返回相应参数
 * @author liugh 53182347@qq.com
 */
public class ResponsePageHelper {

    public ResponsePageHelper() {
    }



    public static <T> ResponsePageModel<T> buildResponseModel(Page<T> page) {
        ResponsePageModel response = new ResponsePageModel();
        response.setCode(HttpStatus.OK.value());
        response.setMsg(HttpStatus.OK.getReasonPhrase());
        response.setData(page.getRecords());
        response.setCount(page.getTotal());
        return response;
    }
}
