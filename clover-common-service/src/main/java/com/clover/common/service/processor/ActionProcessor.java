package com.clover.common.service.processor;

import com.clover.common.base.BusinessException;
import com.clover.common.entity.Order;
import com.clover.common.enums.OrderAction;
import com.clover.common.enums.OrderType;
import com.clover.common.model.OrderModel;
import com.clover.common.service.SpringContextBeanService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * @author liugh
 * @since 2019-05-09
 */
public abstract class ActionProcessor {

    private static final String BEAN_NAME_SUFIX = "Processor";

    private static ActionProcessor getProcessor(OrderAction action, OrderType orderType) throws Exception{
        String beanName = action.name() + orderType.name() + BEAN_NAME_SUFIX;
        ActionProcessor processor = null;
        try{
            processor = SpringContextBeanService.getBean(beanName);
        }catch (NoSuchBeanDefinitionException e){
            throw new BusinessException("未找到对应的流程处理器:" + beanName);
        }
        return processor;
    }

    public static Order process(OrderAction action, OrderType orderType, OrderModel orderDef, Order currentOrder) throws Exception{
        return getProcessor(action,orderType).process(orderDef,currentOrder);
    }

    /**
     * 处理订单,不同的ActionProcessor实现类实现此方法
     * @param orderDef
     * @throws Exception
     */
    public  abstract Order process(OrderModel orderDef,Order currentOrder) throws Exception;


}
