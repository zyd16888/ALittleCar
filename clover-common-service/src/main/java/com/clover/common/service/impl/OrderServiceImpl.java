package com.clover.common.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.clover.common.entity.Order;
import com.clover.common.enums.OrderAction;
import com.clover.common.enums.OrderType;
import com.clover.common.mapper.OrderMapper;
import com.clover.common.model.OrderModel;
import com.clover.common.service.IOrderService;
import com.clover.common.service.handler.OrderHandler;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单主表，当前表只保存流转中的订单信息 服务实现类
 * </p>
 *
 * @author liugh
 * @since 2019-05-09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {


    @Override
    public Order handleOrder(OrderAction action, OrderType orderType, OrderModel orderDef) throws Exception {
        Order order = OrderHandler.handle(action, orderType, orderDef);
        return order;
    }

}
