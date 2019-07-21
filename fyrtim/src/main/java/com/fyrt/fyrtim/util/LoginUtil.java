package com.fyrt.fyrtim.util;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * 用于设置以及判断是否有标志位，如果有标志位，不管标志为值是什么，都表示已经成功登录过。
 */
public class LoginUtil {

    /**
     * 给通道设置LOGIN的值 登录了为true
     *
     * @param channel 通道
     */
    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    /**
     * 查询LOGIN的值 登录了为true
     *
     * @param channel 通道
     * @return 状态
     */
    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
        return loginAttr.get() != null ;
    }

    /**
     * 用户退出登录状态
     * @param channel 通道
     */
    public static void logoOut(Channel channel) {
         channel.attr(Attributes.LOGIN).set(null);
    }
}