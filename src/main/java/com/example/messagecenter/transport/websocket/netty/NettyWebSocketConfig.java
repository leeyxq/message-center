package com.example.messagecenter.transport.websocket.netty;

import com.example.messagecenter.transport.service.MessageTransportService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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

    @Bean(name = "nettyMessageTransportService")
    public MessageTransportService nettyMessageTransportService() {
        return new MessageTransportServiceNettyImpl();
    }
}
