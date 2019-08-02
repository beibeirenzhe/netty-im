package com.flowable.websocketIM.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flowable.websocketIM.entity.PushUnsentMessage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PushUnsentMessageMapper extends BaseMapper<PushUnsentMessage> {


    @Select("SELECT * FROM PUSH_UNSENT_MESSAGE WHERE USER_ID=#{userId}")
    List<PushUnsentMessage>  findAllById(String userId);

    @Delete("DELETE  FROM PUSH_UNSENT_MESSAGE WHERE USER_ID=#{userId}")
    boolean  removeAllById(String userId);
}
