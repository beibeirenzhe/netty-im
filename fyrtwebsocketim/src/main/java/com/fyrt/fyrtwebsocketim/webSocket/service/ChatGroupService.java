package com.fyrt.fyrtwebsocketim.webSocket.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fyrt.fyrtwebsocketim.webSocket.entity.ChatGroup;

import java.util.List;

public interface ChatGroupService extends IService<ChatGroup> {

    List<String> getUserIdByChatGroupId(String chatGroupId);

}
