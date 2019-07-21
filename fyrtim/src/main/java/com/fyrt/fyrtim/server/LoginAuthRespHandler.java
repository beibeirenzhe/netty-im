package com.fyrt.fyrtim.server;

import com.fyrt.fyrtim.util.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;


/**
 * 用户登录 服务端
 */
@Slf4j
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) JsonUtil.parseNettyMessage(msg);
        //未登录
        if (!nettyMessage.isLogin()){
            User user = (User) JsonUtil.parseObject(nettyMessage.getMsg(), User.class);
            boolean flag=true;
            if (user.getAccount().equals("admin") && user.getPassword().equals("test")){
                flag=true;
                UserChannelUtil.bindUser(user,ctx.channel());
            }else {
                flag=false;
            }
            log.info(String.valueOf(flag));
            NettyMessage nettyMessage1=new NettyMessage(TransportType.FLAG.getValue(),flag,ctx.channel());
            ctx.writeAndFlush(JsonUtil.objectToJson(nettyMessage1));
        } else {
            ctx.fireChannelRead(msg);
        }
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }




}
