package com.fyrt.fyrtwebsocketim.webSocket;


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
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


//实现ApplicationRunner接口 tomcat启动时自动运行
@Component
public class WebSocketServer implements ApplicationRunner {



    public static void bind() throws InterruptedException {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup subGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(mainGroup, subGroup)
                    .channel(NioServerSocketChannel.class)
                    //配置NioServerSocketChannel的TCP参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                       // websocket 基于http协议，所以要有http编解码器
                            ch.pipeline().addLast(new HttpServerCodec());
                            //用于支持浏览器和服务器进行WebSocket通信
                            ch.pipeline().addLast(new ChunkedWriteHandler());
//                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                            //将Http消息的多个部门组成一条完整的Http消息
                            ch.pipeline().addLast(new HttpObjectAggregator(65536));
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(6000, 0, 2, 0, 2));
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            /**
                             * 会帮你处理握手动作： handshaking（close, ping, pong） ping + pong = 心跳
                             */
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));

                            // 自定义的handler
                            ch.pipeline().addLast(new WebSocketServerHandler());
                        }
                    });
            //改下端口监听
            ChannelFuture future = server.bind(8082).sync();
            System.out.println("start.....");
            future.channel().closeFuture().sync();
        } finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bind();
    }
}
