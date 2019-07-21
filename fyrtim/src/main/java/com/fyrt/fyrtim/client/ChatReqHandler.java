package com.fyrt.fyrtim.client;

import com.fyrt.fyrtim.util.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Scanner;

@Slf4j
public class ChatReqHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) JsonUtil.parseNettyMessage(msg);
        User user = nettyMessage.getUser();
        if (user != null) {
            System.out.println(user.getUserId() + ":" + nettyMessage.getMsg());
        } else {
            System.out.println(nettyMessage.getMsg());

        }


    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("IM系统准备就绪，请发送消息: ");
        new Thread(() -> {
            while (true) {
                //读取用户在命令行输入的各种数据类型 Terminal控制台输入数据
                Scanner sc = new Scanner(System.in);
                //此扫描器执行当前行，并返回跳过的输入信息
                String line = sc.nextLine();
                HashSet<String> set = new HashSet<>();
                //发给送闪电侠，可自行修改
                set.add("闪电侠");
                NettyMessage nettyMessage = new NettyMessage(TransportType.STRING.getValue(), line, set, ctx.channel(), UserChannelUtil.user());
                String body = JsonUtil.objectToJson(nettyMessage);
                //发送数据并刷新
                ctx.writeAndFlush(body);
            }
        }).start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
