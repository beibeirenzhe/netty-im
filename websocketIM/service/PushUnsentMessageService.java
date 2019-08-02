package com.flowable.websocketIM.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flowable.websocketIM.entity.PushUnsentMessage;

import java.util.List;

public interface PushUnsentMessageService extends IService<PushUnsentMessage> {

    List<PushUnsentMessage> findAllById(String userId);

    boolean  removeAllById(String userId);

}
