package com.fyrt.fyrtwebsocketim.webSocket;


import com.alibaba.fastjson.JSONObject;
import com.fyrt.fyrtwebsocketim.webSocket.entity.ChatGroup;
import com.fyrt.fyrtwebsocketim.webSocket.entity.ChatMessage;
import com.fyrt.fyrtwebsocketim.webSocket.entity.ChatReceiveData;
import com.fyrt.fyrtwebsocketim.webSocket.entity.ChatUser;
import com.fyrt.fyrtwebsocketim.webSocket.service.ChatGroupService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 处理消息的handler
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用于记录和管理所有客户端的channle
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Autowired
    private static ChatGroupService chatGroupService;

    /**
     * 解决注入为null
     */
    static {
        chatGroupService = SpringUtil.getBean(ChatGroupService.class);
    }

    /**
     * 将用户与通道关联信息存在map中
     */
    public static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        ChatMessage parse = JSONObject.parseObject(msg.text(), ChatMessage.class);
        ChatReceiveData chatReceiveData = new ChatReceiveData();
        ChatUser user = JwtUtil.verifyToken(parse.getToken(), "FYRT");
        if (user != null) {
            userIdChannelMap.put(user.getId(), ctx.channel());
            chatReceiveData.setMsg(parse.getMsg());
            chatReceiveData.setUserId(user.getId());
            //判断聊天模式 one等于一对一   other是其他用户
            if (ChatType.ONE_TO_ONE.getValue().equals(parse.getChatType()) && user.getOther() != null) {
                Channel channel = userIdChannelMap.get(user.getOther());
                channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(chatReceiveData)));
                //others 是其他用户组
            } else if (ChatType.MORE_TO_MORE.getValue().equals(parse.getChatType()) && user.getOthers() != null) {
                //如果群聊ID不为null就查询并发送数据
                if (parse.getChatGroupId() != null) {
                    List<String> userIdById = chatGroupService.getUserIdByChatGroupId(parse.getChatGroupId());
                    userIdById.forEach(userId -> {
                        if (userIdChannelMap.containsKey(userId)){
                        Channel channel = userIdChannelMap.get(userId);
                            channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(chatReceiveData)));
                        }else{
                            System.out.println("用户不在线");
                        }
                    });
                } else {
                    //如果群聊ID为null就创建群聊
                    String id = UUID.randomUUID().toString();
                    for (String userId : user.getOthers()) {
                        chatGroupService.save(new ChatGroup(id, userId,user.getChatName()));
                    }
                     chatReceiveData.setChatGroupId(id);
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(chatReceiveData)));
                }

            }
        }
    }


    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channel，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        clients.remove(ctx.channel());
        userIdChannelMap.remove(getKey(userIdChannelMap, ctx.channel()));
        System.out.println("客户端断开");
    }

    /*
     * 功能：读空闲时移除Channel
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            // 判断Channel是否读空闲, 读空闲时移除Channel
            if (event.state().equals(IdleState.READER_IDLE)) {
                userIdChannelMap.remove(getKey(userIdChannelMap, ctx.channel()));
                clients.remove(ctx.channel());
            }
        }
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        userIdChannelMap.remove(getKey(userIdChannelMap, ctx.channel()));
        clients.remove(ctx.channel());
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 根据value取到key
     *
     * @param map     Map
     * @param channel 通道
     * @return
     */
    public static String getKey(Map<String, Channel> map, Channel channel) {
        String key = "";

        for (Map.Entry<String, Channel> m : map.entrySet()) {

            if (m.getValue().equals(channel)) {
                key = m.getKey();
            }
        }
        return key;
    }


}
