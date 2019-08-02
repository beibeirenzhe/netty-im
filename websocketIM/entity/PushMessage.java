package com.flowable.websocketIM.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 推送消息体定义
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushMessage {

    private String token;

    private String msg;

    private String type;

    private String userId;

    private Set<String> others; //需要推送的用户
}
