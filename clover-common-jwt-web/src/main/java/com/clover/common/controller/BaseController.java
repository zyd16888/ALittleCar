package com.clover.common.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dingpengfei
 * @date 2019-05-20 21:15
 */
public class BaseController {

  public  EntityWrapper createTimeRange(EntityWrapper wrapper, String createTimeRange) {
    if (StringUtils.isNoneBlank(createTimeRange)) {
      String[] arr = createTimeRange.split("-");
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      try {
        Date start = sdf.parse(arr[0]);
        Date end = sdf.parse(arr[1]);
        wrapper = (EntityWrapper) wrapper.between("create_time", start, end);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return wrapper;
  }
}
