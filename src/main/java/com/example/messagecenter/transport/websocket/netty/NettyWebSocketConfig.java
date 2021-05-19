package com.example.messagecenter.transport.websocket.netty;

import com.example.messagecenter.common.event.MessagePushEvent;
import com.example.messagecenter.common.vo.MessageVo;
import com.example.messagecenter.transport.MessageTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yeauty.annotation.ServerEndpoint;
import org.yeauty.standard.ServerEndpointExporter;

/**
 * netty原生websocket
 *
 * @author lixiangqian
 * @date 2021/5/17 10:36 上午
 **/
@Configuration
@ConditionalOnClass(ServerEndpoint.class)
public class NettyWebSocketConfig {
    @Bean
    public ServerEndpointExporter nettyServerEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 接受push组件发送的推流事件
     */
    @Bean
    public ApplicationListener<MessagePushEvent> nettyMessagePushEvent(@Autowired @Qualifier("nettyMessageTransportService") MessageTransportService messageTransportService) {
        return event -> messageTransportService.pushToWebsite((MessageVo) event.getSource());
    }

    @Bean(name = "nettyMessageTransportService")
    public MessageTransportService nettyMessageTransportService() {
        return new MessageTransportServiceNettyImpl();
    }
}
