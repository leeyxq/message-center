package com.example.messagecenter.transport;

import com.example.messagecenter.common.event.MessagePushEvent;
import com.example.messagecenter.common.vo.MessageVo;
import com.example.messagecenter.transport.service.MessageTransportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 消息传输自动装配勒
 *
 * @author lixiangqian
 * @date 2021/5/28 10:17 上午
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TransportAutoConfiguration {
    private final List<MessageTransportService> messageTransportServices;

    /**
     * 接受push组件发送的推流事件
     */
    @Bean
    public ApplicationListener<MessagePushEvent> messagePushEventListener() {
        if (CollectionUtils.isEmpty(messageTransportServices)) {
            throw new IllegalStateException("Interface MessageTransportService must have an implementation class");
        }
        return event -> {
            log.debug("Received message from redis:{}", event.getSource());
            messageTransportServices.stream().forEach(service -> service.pushToWebsite((MessageVo) event.getSource()));
        };
    }
}
