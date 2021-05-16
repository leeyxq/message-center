package com.example.messagecenter.receive.service.impl;

import com.example.messagecenter.common.event.MessageSendEvent;
import com.example.messagecenter.common.vo.MessageVo;
import com.example.messagecenter.receive.service.ReceiveMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * 消息接受服务
 *
 * @author lixiangqian
 * @since 2021/5/16 13:43
 */
@Slf4j
@Service
public class ReceiveMessageServiceImpl implements ReceiveMessageService, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void sendMessage(MessageVo messageVo) {
        applicationEventPublisher.publishEvent(new MessageSendEvent(messageVo));
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
