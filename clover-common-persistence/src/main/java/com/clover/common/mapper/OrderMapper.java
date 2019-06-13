package com.clover.common.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.clover.common.entity.Order;

/**
 * <p>
 * 订单主表，当前表只保存流转中的订单信息 Mapper 接口
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
public interface OrderMapper extends BaseMapper<Order> {

}
