package com.clover.common.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.clover.common.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findMenuByRoleCode(@Param("roleCode") String roleCode);
}
