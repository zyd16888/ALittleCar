package com.clover.common.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.clover.common.entity.OperationLog;
import com.clover.common.mapper.OperationLogMapper;
import com.clover.common.service.IOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author dingpengfei
 * @since 2019-05-09
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

}
