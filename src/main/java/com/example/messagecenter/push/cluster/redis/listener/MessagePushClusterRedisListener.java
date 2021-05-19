package com.example.messagecenter.push.cluster.redis.listener;

import com.example.messagecenter.common.event.MessageApplicationEventPublisher;
import com.example.messagecenter.common.event.MessagePushEvent;
import com.example.messagecenter.common.util.JacksonUtil;
import com.example.messagecenter.common.vo.MessageVo;
import lombok.RequiredArgsConstructor;

/**
 * 集群redis-消息发布接受监听
 *
 * @author lixiangqian
 * @since 2021/5/16 13:20
 */
@RequiredArgsConstructor
public class MessagePushClusterRedisListener {
    private final MessageApplicationEventPublisher messageApplicationEventPublisher;

    public void handleMessage(String message) {
        messageApplicationEventPublisher.publishEvent(new MessagePushEvent(JacksonUtil.json2Bean(message, MessageVo.class)));
    }
}