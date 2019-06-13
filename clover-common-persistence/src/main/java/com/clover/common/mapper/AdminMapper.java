package com.clover.common.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.clover.common.entity.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
public interface AdminMapper extends BaseMapper<Admin> {

  /**
   * 等同于编写一个普通 list 查询，mybatis-plus 自动替你分页
   * @author dingpengfei
   * @date 2019-05-09 08:53
   * @param page 分页对象
   * @param info 关键字 模糊查询
   * @param status 状态
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @return java.util.List<Admin>
   */
  List<Admin> selectPageByConditionUser(
      Page<Admin> page,
      @Param("info") String info,
      @Param("status") Integer[] status,
      @Param("startTime") String startTime,
      @Param("endTime") String endTime);
}
