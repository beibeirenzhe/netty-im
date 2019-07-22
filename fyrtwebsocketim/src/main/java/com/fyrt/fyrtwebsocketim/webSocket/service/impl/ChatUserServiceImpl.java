package com.fyrt.fyrtwebsocketim.webSocket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyrt.fyrtwebsocketim.webSocket.entity.ChatUser;
import com.fyrt.fyrtwebsocketim.webSocket.mapper.ChatUserMapper;
import com.fyrt.fyrtwebsocketim.webSocket.service.ChatUserService;
import org.springframework.stereotype.Service;

@Service
public class ChatUserServiceImpl extends ServiceImpl<ChatUserMapper, ChatUser> implements ChatUserService {
}
