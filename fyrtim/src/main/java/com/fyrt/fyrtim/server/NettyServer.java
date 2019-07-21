package com.fyrt.fyrtim.server;

import com.fyrt.fyrtim.util.MessagePackDecoder;
import com.fyrt.fyrtim.util.MessagePackEncoder;
import com.fyrt.fyrtim.util.NettyConstants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyServer {

    public void bind(int port) throws InterruptedException {


        //server端引导类
        //bossGroup的线程数
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        //连接池处理数据
        //worker的线程数
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    //长连接定义队列大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //开启长连接
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    //日志打印设置
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //设置childHandler执行所有的连接请求
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //用户每次请求都会从第一个Handler开始
                            //设置超时时间
                            ch.pipeline().addLast(new IdleStateHandler(150,150,300, TimeUnit.SECONDS));
                            //定长解码器
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(6000, 0, 2, 0, 2));
                            //增加解码器
                            ch.pipeline().addLast(new MessagePackDecoder());
                            //这里设置读取报文的包头长度来避免粘包
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            //增加编码器
                            ch.pipeline().addLast(new MessagePackEncoder());
                            //心跳续约
                            ch.pipeline().addLast(new HeartBeatRespHandler());
                            //登录握手
                            ch.pipeline().addLast(new LoginAuthRespHandler());
                            ch.pipeline().addLast(new ChatRespHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("server: start ok ! host:" + NettyConstants.REMOTE_IP + ",port:" + NettyConstants.REMOTE_PORT);
            future.channel().closeFuture().sync();
        }finally {
            //优雅退出
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer().bind(NettyConstants.REMOTE_PORT);

    }


}
