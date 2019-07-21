package com.fyrt.fyrtim.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * msg 传输类型
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TransportType {

    STRING("string类型","string"),
    OBJECT("object类型","object"),
    FLAG("flag类型","flag"),
    HEARTBEAT("心跳机制","heartbeat");

    private String msg;

    private String value;
}
