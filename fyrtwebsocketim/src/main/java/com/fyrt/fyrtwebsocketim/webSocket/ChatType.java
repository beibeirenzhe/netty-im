package com.fyrt.fyrtwebsocketim.webSocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 聊天类型
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ChatType {

    ONE_TO_ONE("一对一聊天","one"),
    MORE_TO_MORE("多对多聊天","more");


    private String msg;

    private String value;
}
