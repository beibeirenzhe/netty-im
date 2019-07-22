package com.fyrt.fyrtwebsocketim.webSocket.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群聊
 */
//  CREATE TABLE "CHAT"."CHAT_GROUP"
//   (	"CHAT_GROUP_ID" VARCHAR2(255 BYTE),
//	"USER_ID" VARCHAR2(255 BYTE),
//	"CHAT_NAME" VARCHAR2(255 BYTE)
//   ) SEGMENT CREATION IMMEDIATE
//  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
//  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
//  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
//  TABLESPACE "USERS" ;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGroup extends Model<ChatGroup> {

    //组id
    private String chatGroupId;

    //用户id
    private String userId;

    //群聊名称
    private String chatName;




}
