package com.example.messagecenter.receive.service.impl;

import com.example.messagecenter.common.event.MessageApplicationEventPublisher;
import com.example.messagecenter.common.event.MessageSendEvent;
import com.example.messagecenter.common.vo.MessageVo;
import com.example.messagecenter.receive.service.ReceiveMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 消息接受服务
 *
 * @author lixiangqian
 * @since 2021/5/16 13:43
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveMessageServiceImpl implements ReceiveMessageService {
    private final MessageApplicationEventPublisher messageApplicationEventPublisher;

    @Override
    public void sendMessage(MessageVo messageVo) {
        messageApplicationEventPublisher.publishEvent(new MessageSendEvent(messageVo));
    }
}
