package com.example.messagecenter.push.single.listener;

import com.example.messagecenter.common.event.MessageApplicationEventPublisher;
import com.example.messagecenter.common.event.MessagePushEvent;
import com.example.messagecenter.common.event.MessageSendEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * 单机-消息发布接受监听
 *
 * @author lixiangqian
 * @since 2021/5/16 13:20
 */
@Slf4j
@RequiredArgsConstructor
public class SingleMessageSendListener implements ApplicationListener<MessageSendEvent> {
    private final MessageApplicationEventPublisher messageApplicationEventPublisher;

    @Override
    public void onApplicationEvent(MessageSendEvent event) {
        log.debug("receive MessageSendEvent: {}", event.getSource());
        messageApplicationEventPublisher.publishEvent(new MessagePushEvent(event.getSource()));
    }
}
