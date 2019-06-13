package com.clover.common.model;

import com.github.crab2died.converter.ReadConvertible;

/**
 * excel是否开除 列数据转换器
 */
public class Student2ExpelConverter implements ReadConvertible {

    @Override
    public Object execRead(String object) {

        return "是".equals(object);
    }
}
