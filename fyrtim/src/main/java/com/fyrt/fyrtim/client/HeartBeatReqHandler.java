package com.fyrt.fyrtim.client;

import com.alibaba.fastjson.JSONObject;
import com.fyrt.fyrtim.util.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


/**
 * 心跳检测 客户端
 */
@Slf4j
public class HeartBeatReqHandler extends SimpleChannelInboundHandler {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage= (NettyMessage) JsonUtil.parseNettyMessage(msg);
        if (LoginUtil.hasLogin(ctx.channel())) {
            if (TransportType.HEARTBEAT.getValue().equals(nettyMessage.getTransportType())) {
                if (nettyMessage.getMsg().equals("pong")) {
                    log.info("收到pong请求");
                    log.info("发送ping请求");
                    ctx.writeAndFlush(JSONObject.toJSONString(new NettyMessage(TransportType.HEARTBEAT.getValue(), "ping", ctx.channel())));
                } else if (nettyMessage.getMsg().equals("ok")) {
                    log.info("心跳续约:" + nettyMessage.getMsg());
                }
            }else{
                //直接跳转到下个handler请求
                ctx.fireChannelRead(msg);
            }
        } else {
            //直接跳转到下个handler请求
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //直接跳转到下个handler请求
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //退出登录
        UserChannelUtil.unBindUser(ctx.channel());
        LoginUtil.logoOut(ctx.channel());
        System.out.println("客户端已关闭");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)obj;
            if (event.state()== IdleState.READER_IDLE){
                log.info("客户端读超时");
            }else if (event.state()== IdleState.WRITER_IDLE){
                log.info("客户端写超时");
            }else if (event.state()==IdleState.ALL_IDLE){
                log.info("客户端所有操作超时");
            }
            ctx.writeAndFlush(JSONObject.toJSONString(new NettyMessage(TransportType.HEARTBEAT.getValue(),"ping",ctx.channel())));
        }

    }
}
