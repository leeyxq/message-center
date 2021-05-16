package com.example.messagecenter.push.cluster.redis.listener;

import com.example.messagecenter.common.event.MessageSendEvent;
import com.example.messagecenter.common.util.JacksonUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import static com.example.messagecenter.common.constant.CommonConst.REDIS_CHANNEL_SENG_SMG;

/**
 * 单机-消息发布接受监听
 *
 * @author lixiangqian
 * @since 2021/5/16 13:20
 */
public class MessageSendClusterRedisListener implements ApplicationListener<MessageSendEvent> {
    private StringRedisTemplate stringRedisTemplate;

    public MessageSendClusterRedisListener(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void onApplicationEvent(MessageSendEvent event) {
        stringRedisTemplate.convertAndSend(REDIS_CHANNEL_SENG_SMG, JacksonUtil.bean2Json(event.getSource()));
    }
}
