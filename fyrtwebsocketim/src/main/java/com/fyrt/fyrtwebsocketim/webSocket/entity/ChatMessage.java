package com.fyrt.fyrtwebsocketim.webSocket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private String msg;   //消息

    private String token; //用户token认证

    private String chatType; //聊天类型 群聊 or 单聊

    private String chatGroupId;//群聊ID
}
