package com.fyrt.fyrtim.util;

import com.alibaba.fastjson.JSON;


public class JsonUtil {


    /**
     * 对象处理
     *
     * @param msg    数据
     * @param classs 实体类class
     * @return 对象
     */
    public static Object parseObject(Object msg, Class classs) {
        Object o = JSON.parseObject(msg.toString(), classs);
        return o;
    }


    /**
     * 对象处理
     *
     * @param nettyMessage 数据
     * @return 对象
     */
    public static Object parseNettyMessage(Object nettyMessage) {
        Object obj = null;
        String msgString = nettyMessage.toString();
        msgString = msgString.replaceAll("\\\\", "");
        msgString = msgString.substring(1, msgString.length() - 1);
        //先取出类型
        int typeIndex = msgString.indexOf("transportType");
        //当前用户对象
        int userIndex = msgString.indexOf("user");
        //传输的用户组
        int otherUsersIndex = msgString.lastIndexOf("otherUsers");
        String substring = "";
        if (userIndex != -1 && otherUsersIndex != -1) {
            substring = msgString.substring(typeIndex + 16, userIndex - 3);
        } else {
            substring = msgString.substring(typeIndex + 16, msgString.length() - 2);
        }
        StringBuffer stringBuffer = new StringBuffer(msgString);
        if (substring.equals(TransportType.OBJECT.getValue())) {
            int first = msgString.indexOf(",");
            int last = msgString.lastIndexOf(",");
            stringBuffer = stringBuffer.replace(first + 7, first + 9, "{").replace(last - 3, last - 1, "}");
            obj = JSON.parseObject(stringBuffer.toString(), NettyMessage.class);
        } else if (substring.equals(TransportType.STRING.getValue()) || substring.equals(TransportType.HEARTBEAT.getValue())) {
            int msgIndex = msgString.indexOf("msg");
            int otherUsers = msgString.indexOf("otherUsers");
            if (otherUsers != -1) {
                stringBuffer = stringBuffer.replace(msgIndex + 5, msgIndex + 7, "\"").replace(otherUsersIndex - 5, otherUsersIndex - 3, "\"");
            } else {
                int lastIndex = msgString.lastIndexOf(",");
                stringBuffer = stringBuffer.replace(msgIndex + 5, msgIndex + 7, "\"").replace(lastIndex - 3, lastIndex - 1, "\"");
            }
            obj = JSON.parseObject(stringBuffer.toString(), NettyMessage.class);

        } else {
            obj = JSON.parseObject(msgString, NettyMessage.class);

        }
        return obj;
    }

    public static String stringMsg(Object msg) {
        String stringMsg = msg.toString().substring(1, msg.toString().length() - 1);
        return stringMsg;
    }

    public static boolean booleanToJson(Object msg) {
        Boolean booleanMsg = Boolean.valueOf(msg.toString());
        return booleanMsg;
    }


    public static String objectToJson(Object object) {
        String body = JSON.toJSONString(object);
        return body;
    }


}
