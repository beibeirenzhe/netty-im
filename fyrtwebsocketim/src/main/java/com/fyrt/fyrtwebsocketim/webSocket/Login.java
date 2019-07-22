package com.fyrt.fyrtwebsocketim.webSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fyrt.fyrtwebsocketim.webSocket.entity.ChatUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@Slf4j
public class Login {




    @PostMapping("login")
    @ResponseBody
    public  String login(ChatUser user) throws JsonProcessingException {
        String token = JwtUtil.createToken(user,10000);
        System.out.println("当前登录用户:"+user.getId());
        return  token;
    }


    @GetMapping("testLog")
    @ResponseBody
    public void testLog(){
        log.debug("记录debug日志");
        log.info("访问了index方法");
        log.error("记录了error错误日志");
    }



}
