package com.flowable.websocketIM.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flowable.websocketIM.entity.PushUnsentMessage;
import com.flowable.websocketIM.mapper.PushUnsentMessageMapper;
import com.flowable.websocketIM.service.PushUnsentMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushUnsentMessageServiceImpl extends ServiceImpl<PushUnsentMessageMapper, PushUnsentMessage> implements PushUnsentMessageService {

    @Autowired
    private PushUnsentMessageMapper pushUnsentMessageMapper;

    @Override
    public List<PushUnsentMessage> findAllById(String userId) {
        return pushUnsentMessageMapper.findAllById(userId);
    }

    @Override
    public boolean removeAllById(String userId) {
        return pushUnsentMessageMapper.removeAllById(userId);
    }
}
