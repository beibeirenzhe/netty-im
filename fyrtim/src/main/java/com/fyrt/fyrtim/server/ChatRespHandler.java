package com.fyrt.fyrtim.server;

import com.fyrt.fyrtim.util.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Set;

public class ChatRespHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) JsonUtil.parseNettyMessage(msg);
        //用户组
        Set<String> userIds = nettyMessage.getOtherUsers();
        if (userIds.size() > 0) {
            userIds.forEach(userId -> {
                if (UserChannelUtil.userIdChannelMap.get(userId) == null) {
                    System.out.println("用户不在线");
                } else {
                    Channel channel = UserChannelUtil.getChannel(userId);
                    User user = UserChannelUtil.getUser(channel);
                    System.out.println(UserChannelUtil.userIdChannelMap);
                    System.out.println(channel);
                    System.out.println(user);
                    //发送到指定通道
                    channel.writeAndFlush(JsonUtil.objectToJson(new NettyMessage(TransportType.STRING.getValue(), nettyMessage.getMsg(), user)));

                }
            });
        } else {
            System.out.println("服务端接收数据:"+nettyMessage.getMsg());
            ctx.writeAndFlush(JsonUtil.objectToJson(new NettyMessage(TransportType.STRING.getValue(), nettyMessage.getMsg(), ctx.channel())));
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
