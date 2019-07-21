package com.fyrt.fyrtim.client;

import com.alibaba.fastjson.JSONObject;
import com.fyrt.fyrtim.util.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;


/**
 * 用户登录 客户端
 */
@Slf4j
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage =(NettyMessage) JsonUtil.parseNettyMessage(msg);
        if (JsonUtil.booleanToJson(nettyMessage.getMsg())) {
            LoginUtil.markAsLogin(ctx.channel());
            log.info(UserChannelUtil.user().getUserId()+"登录成功");
            // 一行代码实现逻辑的删除 删除LoginAuthReqHandler
            ctx.pipeline().remove(this);
            ctx.fireChannelActive();
        }else{
            log.info("登录失败");
            ctx.channel().close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyMessage nettyMessage = new NettyMessage(TransportType.OBJECT.getValue(),UserChannelUtil.user(),ctx.channel());
        String body = JSONObject.toJSONString(nettyMessage);
        ctx.writeAndFlush(body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (LoginUtil.hasLogin(ctx.channel())) {
            System.out.println("当前连接登录验证完毕，无需再次验证");
        } else {
            System.out.println("无登录验证，强制关闭连接!");
        }
    }

}
