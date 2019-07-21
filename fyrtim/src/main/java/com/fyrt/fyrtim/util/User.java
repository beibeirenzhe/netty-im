package com.fyrt.fyrtim.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.msgpack.annotation.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Message
public class User{

    private String userId;

    private String account;

    private String password;

}
