package com.clover.common.controller;

import com.clover.common.config.web.http.ResponseHelper;
import com.clover.common.config.web.http.ResponseModel;
import com.clover.common.entity.Order;
import com.clover.common.enums.OrderAction;
import com.clover.common.enums.OrderType;
import com.clover.common.model.OrderModel;
import com.clover.common.service.IOrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liugh
 * @since 2019-05-09
 */
@RestController
public class OrderController {

    @Resource
    private IOrderService orderService;

    /**
     * 测试工作流
     * 订单发货(动作),待发货-->已发货(状态)
     * @param orderType
     * @param orderModel
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/deliver/{orderType}")
    public ResponseModel<Order> updateDeliver(@PathVariable String orderType, @RequestBody OrderModel orderModel)
            throws Exception {
        Order orderDef = orderService.handleOrder(OrderAction.deliver, OrderType.getInstance(orderType), orderModel);
        return ResponseHelper.buildResponseModel(orderDef);
    }


}
