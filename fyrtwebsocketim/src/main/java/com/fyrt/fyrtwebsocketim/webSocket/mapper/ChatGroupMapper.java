package com.fyrt.fyrtwebsocketim.webSocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fyrt.fyrtwebsocketim.webSocket.entity.ChatGroup;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ChatGroupMapper extends BaseMapper<ChatGroup> {


   @Select("SELECT USER_ID FROM CHAT_GROUP WHERE CHAT_GROUP_ID=#{chatGroupId}")
   List<String> getUserIdByChatGroupId(String chatGroupId);
}
