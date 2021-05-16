package com.example.messagecenter.push.single.listener;

import com.example.messagecenter.common.event.MessagePushEvent;
import com.example.messagecenter.common.event.MessageSendEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;

/**
 * 单机-消息发布接受监听
 *
 * @author lixiangqian
 * @since 2021/5/16 13:20
 */
@Slf4j
public class SingleMessageSendListener implements ApplicationListener<MessageSendEvent>, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onApplicationEvent(MessageSendEvent event) {
        log.debug("receive MessageSendEvent: {}", event.getSource());
        applicationEventPublisher.publishEvent(new MessagePushEvent(event.getSource()));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
