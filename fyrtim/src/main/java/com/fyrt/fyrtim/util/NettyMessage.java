package com.fyrt.fyrtim.util;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NettyMessage {

    //传输类型
    private String transportType;

    //传输内容
    private String msg;

    //是否登录
    private boolean isLogin;

    //其他用户
    private Set<String> otherUsers;

    //当前用户
    private User user;

    //聊天类型
    private String ChatType;


    public NettyMessage(String transportType, Object msg, Channel channel) {
        this.transportType = transportType;
        this.msg = JsonUtil.objectToJson(msg);
        this.isLogin = LoginUtil.hasLogin(channel);
    }

    public NettyMessage(String transportType, Object object, Set<String> otherUsers, Channel channel) {
        this.transportType = transportType;
        this.msg = JsonUtil.objectToJson(object);
        this.isLogin = LoginUtil.hasLogin(channel);
        this.otherUsers = otherUsers;
    }

    public NettyMessage(String transportType, Object msg, Set<String> otherUsers, Channel channel, User user) {
        this.transportType = transportType;
        this.msg = JsonUtil.objectToJson(msg);
        this.isLogin = LoginUtil.hasLogin(channel);
        this.otherUsers = otherUsers;
        this.user = user;
    }

    public NettyMessage(String transportType, String msg) {
        this.transportType = transportType;
        this.msg = msg;
    }


    public NettyMessage(String transportType, String msg, User user) {
        this.transportType = transportType;
        this.msg = msg;
        this.user = user;
    }

    public NettyMessage(String transportType, String msg, Set<String> otherUsers) {
        this.transportType = transportType;
        this.msg = msg;
        this.otherUsers = otherUsers;
    }

    public NettyMessage(String transportType, String msg, Set<String> otherUsers, User user, String chatType) {
        this.transportType = transportType;
        this.msg = msg;
        this.otherUsers = otherUsers;
        this.user = user;
        ChatType = chatType;
    }
}
