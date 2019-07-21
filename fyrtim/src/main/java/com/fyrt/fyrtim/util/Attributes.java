package com.fyrt.fyrtim.util;

import io.netty.util.AttributeKey;

/**
 * 通道中的自定义属性
 */
public interface Attributes {

    /**
     * 用户控制用户是否登录   newInstance创建一个给定名称为login的key，如果已存在则会报错
     */
    AttributeKey<Boolean> LOGIN=AttributeKey.newInstance("login");

    /**
     * 用户存储用户信息到通道中
     */
    AttributeKey<User> USER=AttributeKey.newInstance("user");

}
