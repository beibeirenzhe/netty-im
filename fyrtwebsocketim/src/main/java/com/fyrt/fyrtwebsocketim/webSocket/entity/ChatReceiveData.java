package com.fyrt.fyrtwebsocketim.webSocket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回调信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatReceiveData {

    //当前用户id
    private String  userId;


    //组id
    private String chatGroupId;


    //回调信息
    private String msg;
}
