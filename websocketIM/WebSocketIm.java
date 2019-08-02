package com.flowable.websocketIM;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.flowable.websocketIM.entity.PushMessage;
import com.flowable.websocketIM.entity.PushUnsentMessage;
import com.flowable.websocketIM.service.PushUnsentMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/IM/{userId}")
@Component
@Slf4j
public class WebSocketIm {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //新：使用map对象，便于根据userId来获取对应的WebSocket
    private static ConcurrentHashMap<String, WebSocketIm> websocketList = new ConcurrentHashMap<>();
    //接收sid
   private String  userId="";

    @Autowired
    private static PushUnsentMessageService pushService;

    private static PushUnsentMessage percentagePush = new PushUnsentMessage(new Date(System.currentTimeMillis()), "percentage");


    /**
     * 解决注入为null
     */
    static {
        pushService = SpringUtil.getBean(PushUnsentMessageService.class);
    }

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        websocketList.put(userId,this);
//        log.info("websocketList->"+JSON.toJSONString(websocketList));
//        addOnlineCount();           //在线数加1
//        log.info("有新窗口开始监听:"+userId+",当前在线人数为" + getOnlineCount());
        this.userId=userId;
        try {
            List<PushUnsentMessage> byId = pushService.findAllById(userId);
            pushService.removeAllById(userId);
            sendMessage(JSON.toJSONString(byId));
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(websocketList.get(this.userId)!=null){
            websocketList.remove(this.userId);
//            subOnlineCount();           //在线数减1
//            log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if(StringUtils.isNotBlank(message)){
            PushMessage pushMessage= JSONObject.parseObject(message, PushMessage.class);
            if (pushMessage.getOthers().size()>0) {
                Iterator<String> iterator = pushMessage.getOthers().iterator();
                while (iterator.hasNext()){
                    String userId = iterator.next();
                    if (websocketList.containsKey(userId)){
                        WebSocketIm channel = websocketList.get(userId);
                         List<PushUnsentMessage> list = new ArrayList<>();
                         list.add(new PushUnsentMessage(userId, pushMessage.getMsg(), new Date(System.currentTimeMillis())));
                        channel.session.getBasicRemote().sendText(JSONObject.toJSONString(list,SerializerFeature.DisableCircularReferenceDetect));
                    }else{
                        PushUnsentMessage pushUnsentMessage = new PushUnsentMessage(userId,pushMessage.getMsg(),new Date(System.currentTimeMillis()));
                        pushService.save(pushUnsentMessage);
                    }
                }
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        websocketList.remove(this.userId);
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userIdList) throws IOException {
        List<String> userIds = JSON.parseObject(userIdList,  new TypeReference<List<String>>(){});
        PushUnsentMessage pushUnsentMessage = new PushUnsentMessage();
        pushUnsentMessage.setMsg(message);
        pushUnsentMessage.setTime(new Date(System.currentTimeMillis()));
        List<PushUnsentMessage> list = new ArrayList<>();
        Iterator<Map.Entry<String, WebSocketIm>> iterator = websocketList.entrySet().iterator();
            while (iterator.hasNext()){
                System.out.println(iterator.next().getKey());
                String key = iterator.next().getKey();
              if (userIds.size()==0){
                pushUnsentMessage.setUserId(key);
                WebSocketIm channel = websocketList.get(key);
                channel.session.getBasicRemote().sendText(JSONObject.toJSONString(list,SerializerFeature.DisableCircularReferenceDetect));
                }else{
                    for (String userId: userIds){
                        if (userId.equals(key)){
                            pushUnsentMessage.setUserId(userId);
                            pushUnsentMessage.setType("back");
                            list.add(pushUnsentMessage);
                            WebSocketIm channel = websocketList.get(key);
                            channel.session.getBasicRemote().sendText(JSONObject.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect));
                        }else{
                            pushService.save(new PushUnsentMessage(userId,message,new Date(System.currentTimeMillis()),"back"));
                        }
                    }
                }
            }
    }

    /**
     * 发送进度条
     */
    public static void  sendPercentage(String message,@PathParam("userId") String userId) throws IOException, InterruptedException {
        Set<PushUnsentMessage> set=new HashSet<>();
        WebSocketIm webSocketIm = websocketList.get(userId);
        percentagePush.setMsg(message);
        set.add(percentagePush);
        webSocketIm.session.getBasicRemote().sendText(JSONObject.toJSONString(set,SerializerFeature.DisableCircularReferenceDetect));
        Thread.sleep(1000);
    }




    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketIm.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketIm.onlineCount--;
    }
}


