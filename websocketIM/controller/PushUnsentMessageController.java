package com.flowable.websocketIM.controller;

import com.flowable.websocketIM.entity.PushUnsentMessage;
import com.flowable.websocketIM.service.PushUnsentMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PushUnsentMessageController {

    @Autowired
    private PushUnsentMessageService pushUnsentMessageService;

    @GetMapping("getUnsentMessage/{id}")
    @ResponseBody
    public List<PushUnsentMessage> getUnsentMessage(@PathVariable("id") String id){
        List<PushUnsentMessage> byId = pushUnsentMessageService.findAllById(id);
         pushUnsentMessageService.removeAllById(id);
        return byId;
    }
}
