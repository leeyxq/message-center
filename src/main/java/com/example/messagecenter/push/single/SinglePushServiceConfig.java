package com.example.messagecenter.push.single;

import com.example.messagecenter.common.event.MessageApplicationEventPublisher;
import com.example.messagecenter.push.single.listener.SingleMessageSendListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.messagecenter.common.constant.CommonConst.RUN_MODEL_CONFIG;
import static com.example.messagecenter.common.constant.CommonConst.RUN_MODEL_SINGLE;

/**
 * 消息推送服务-单机配置
 *
 * @author lixiangqian
 * @since 2021/5/16 12:56
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = RUN_MODEL_CONFIG, havingValue = RUN_MODEL_SINGLE, matchIfMissing = true)
public class SinglePushServiceConfig {

    /**
     * 订阅spring event事件-推送消息
     */
    @Bean
    public SingleMessageSendListener singleMessageSendListener(MessageApplicationEventPublisher messageApplicationEventPublisher) {
        return new SingleMessageSendListener(messageApplicationEventPublisher);
    }
}
