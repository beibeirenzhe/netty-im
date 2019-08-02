package com.flowable.websocketIM;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.flowable.websocketIM.entity.PushUnsentMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class test {


    /**
     * 发送消息demo
     * @throws IOException
     */
    @GetMapping("testIM")
    @ResponseBody
    public void testIM() throws IOException {
        List<String> list=new ArrayList<>();
        list.add("1");
        list.add("2");
        String lists = JSON.toJSONString(list);
        WebSocketIm.sendInfo("这是一个测试", lists);
    }

    /**
     * 发送进度条demo
     */
    @GetMapping("testPercentage")
    @ResponseBody
    public  void  testPercentage() throws IOException, InterruptedException {
        WebSocketIm.sendPercentage("100","2");

    }
}
