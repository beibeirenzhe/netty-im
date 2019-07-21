package com.fyrt.fyrtim.server;

import com.alibaba.fastjson.JSONObject;
import com.fyrt.fyrtim.util.JsonUtil;
import com.fyrt.fyrtim.util.NettyMessage;
import com.fyrt.fyrtim.util.TransportType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳检测 服务端
 */
@Slf4j
public class HeartBeatRespHandler extends SimpleChannelInboundHandler {



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) JsonUtil.parseNettyMessage(msg);
        if (nettyMessage.isLogin()){
            if (TransportType.HEARTBEAT.getValue().equals(nettyMessage.getTransportType())) {
                log.info("收到客户端pong请求");
                log.info("发送ping请求");
                ctx.writeAndFlush(JSONObject.toJSONString(new NettyMessage(TransportType.HEARTBEAT.getValue(), "ok", ctx.channel())));
            }else{
                ctx.fireChannelRead(msg);
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //退出登录
        System.out.println("服务端已关闭");
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
        }

    }
}
