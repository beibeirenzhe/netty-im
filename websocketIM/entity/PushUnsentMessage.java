package com.flowable.websocketIM.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//
//  CREATE TABLE "SCYJ"."PUSH_UNSENT_MESSAGE"
//   (	"USER_ID" VARCHAR2(255 BYTE),
//	"MSG" VARCHAR2(255 BYTE),
//	"TIME" DATE,
//	"TYPE" VARCHAR2(255 BYTE)
//   )

/**
 * 消息推送返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushUnsentMessage extends Model<PushUnsentMessage> {

    private String userId;

    private String msg;

    private Date time;

    private String type;

    public PushUnsentMessage(String userId, String msg, Date time) {
        this.userId = userId;
        this.msg = msg;
        this.time = time;
        this.type = "push";
    }

    public PushUnsentMessage(String msg, Date time, String type) {
        this.msg = msg;
        this.time = time;
        this.type = type;
    }

    public PushUnsentMessage(Date time, String type) {
        this.time = time;
        this.type = type;
    }
}
