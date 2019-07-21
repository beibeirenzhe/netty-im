package com.fyrt.fyrtim.util;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户通道绑定
 */
public class UserChannelUtil {

    /**
     * 将用户与通道关联信息存在map中
     */
    public static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    /**
     * 将用户信息绑定至管道中
     * @param user 用户
     * @param channel
     */
    public static void bindUser(User user, Channel channel) {
        userIdChannelMap.put(user.getUserId(), channel);
        channel.attr(Attributes.USER).set(user);
    }


    /**
     * 程序退出时解除绑定
     * @param channel
     */
    public static void unBindUser(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getUser(channel).getUserId());
            channel.attr(Attributes.USER).set(null);
        }
    }

    /**
     * 判断通道中是否存在用户信息，存在则已经登录
     */
    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(Attributes.USER);
    }

    /**
     * 从通道中获取USER属性
     */
    public static User getUser(Channel channel) {
        return channel.attr(Attributes.USER).get();
    }

    /**
     * 通过用户ID获取通道
     */
    public static Channel getChannel(String userId) {
        return userIdChannelMap.get(userId);
    }


    public static User user(){
        User user = new User("蝙蝠侠","admin","test");
        return user;
    }
}
