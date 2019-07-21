package com.fyrt.fyrtim.client;

import com.fyrt.fyrtim.util.MessagePackDecoder;
import com.fyrt.fyrtim.util.MessagePackEncoder;
import com.fyrt.fyrtim.util.NettyConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyClient {
    EventLoopGroup group = new NioEventLoopGroup();
    private static final int MAX_RETRY = 5; //最大重连次数
    public void connect(int port, String host) {

        //client NIO thread
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    //日志打印
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //设置超时时间
                            ch.pipeline().addLast(new IdleStateHandler(150,150,300,TimeUnit.SECONDS));
                            //这里设置通过增加包头表示报文长度来避免粘包
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(6000, 0, 2,0,2));                            ch.pipeline().addLast("msgpack decoder",new MessagePackDecoder());
                            //增加解码器
                            ch.pipeline().addLast(new MessagePackDecoder());
                            //这里设置读取报文的包头长度来避免粘包
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            //增加编码器
                            ch.pipeline().addLast(new MessagePackEncoder());
                            //心跳续约
                            ch.pipeline().addLast(new HeartBeatReqHandler());
                            //登录校验
                            ch.pipeline().addLast(new LoginAuthReqHandler());
                            ch.pipeline().addLast(new ChatReqHandler());
                        }
                    });

            //异步连接
            connect(b, host, port, MAX_RETRY);
        }finally {
            //优雅退出
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyClient().connect(NettyConstants.LOCAL_PORT, NettyConstants.LOCAL_IP);

    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        ChannelFuture future1 = bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                log.info(new Date() + ": 连接成功!");
            } else if (retry == 0) {
                log.info("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                log.info(new Date() + ": 连接失败，第" + order + "次重连");
                bootstrap.group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
        try {
            future1.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
