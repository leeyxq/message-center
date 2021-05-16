package com.example.messagecenter.push.cluster.redis.listener;

import com.example.messagecenter.common.event.MessagePushEvent;
import com.example.messagecenter.common.util.JacksonUtil;
import com.example.messagecenter.common.util.SpringUtil;
import com.example.messagecenter.common.vo.MessageVo;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 集群redis-消息发布接受监听
 *
 * @author lixiangqian
 * @since 2021/5/16 13:20
 */
public class MessagePushClusterRedisListener {

    public void handleMessage(String message) {
        SpringUtil.getBean(ApplicationEventPublisher.class).publishEvent(new MessagePushEvent(JacksonUtil.json2Bean(message, MessageVo.class)));
    }
}