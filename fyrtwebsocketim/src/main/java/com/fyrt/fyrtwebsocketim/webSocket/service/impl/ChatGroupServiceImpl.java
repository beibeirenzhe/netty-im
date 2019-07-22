package com.fyrt.fyrtwebsocketim.webSocket.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyrt.fyrtwebsocketim.webSocket.entity.ChatGroup;
import com.fyrt.fyrtwebsocketim.webSocket.mapper.ChatGroupMapper;
import com.fyrt.fyrtwebsocketim.webSocket.service.ChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatGroupServiceImpl extends ServiceImpl<ChatGroupMapper, ChatGroup> implements ChatGroupService {

    @Autowired
    private ChatGroupMapper chatGroupMapper;

    @Override
    public List<String> getUserIdByChatGroupId(String chatGroupId) {
        return chatGroupMapper.getUserIdByChatGroupId(chatGroupId);
    }
}
