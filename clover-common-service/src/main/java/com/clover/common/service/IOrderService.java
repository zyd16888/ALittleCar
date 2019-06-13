package com.clover.common.service;

import com.baomidou.mybatisplus.service.IService;
import com.clover.common.entity.Order;
import com.clover.common.enums.OrderAction;
import com.clover.common.enums.OrderType;
import com.clover.common.model.OrderModel;

/**
 * <p>
 * 订单主表，当前表只保存流转中的订单信息 服务类
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
public interface IOrderService extends IService<Order> {

    Order handleOrder(OrderAction action, OrderType orderType, OrderModel orderDef) throws Exception;

}
