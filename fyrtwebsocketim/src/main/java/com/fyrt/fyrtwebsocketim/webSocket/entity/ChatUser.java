package com.fyrt.fyrtwebsocketim.webSocket.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

//  CREATE TABLE "CHAT"."CHAT_USER"
//   (	"USER_ID" VARCHAR2(255 BYTE),
//	"USER_NAME" VARCHAR2(255 BYTE),
//	"USER_ACCOUNT" VARCHAR2(255 BYTE),
//	"USER_PASSWORD" VARCHAR2(255 BYTE)
//   ) SEGMENT CREATION IMMEDIATE
//  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
//  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
//  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
//  TABLESPACE "USERS" ;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser {

    @TableId
    @TableField("user_id")
    private String id;

    @TableField("user_name")
    private String name;

    @TableField("user_account")
    private String account;

    @TableField("user_password")
    private String password;

    @TableField(exist = false)
    private String other; //其他用户

    @TableField(exist = false)
    private Set<String> others;//其他用户组

    @TableField(exist = false)
    private String chatName;//群聊名称



}
