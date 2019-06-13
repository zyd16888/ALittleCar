package com.clover.common.service.handler;

import com.clover.common.entity.Order;
import com.clover.common.enums.OrderAction;
import com.clover.common.enums.OrderType;
import com.clover.common.model.OrderModel;
import com.clover.common.service.processor.ActionProcessor;
import org.springframework.stereotype.Component;

/**
 * @author liugh
 * @since 2019-05-09
 */
@Component("ProductOrderHandler")
public class ProductOrderHandler extends OrderHandler {

	@Override
	public Order handleInternal(OrderAction action, OrderType orderType, OrderModel orderDef, Order currentOrder) throws Exception {
		return  ActionProcessor.process(action,orderType,orderDef,currentOrder);
	}
}
