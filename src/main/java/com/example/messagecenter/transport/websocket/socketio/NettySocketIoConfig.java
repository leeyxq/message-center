package com.example.messagecenter.transport.websocket.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.example.messagecenter.common.event.MessagePushEvent;
import com.example.messagecenter.common.vo.MessageVo;
import com.example.messagecenter.config.WebSocketConfig;
import com.example.messagecenter.transport.MessageTransportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @author lixiangqian
 * @since 2021/5/16 15:20
 */
@Slf4j
@Configuration
@ConditionalOnClass(SocketIOServer.class)
public class NettySocketIoConfig {
    /**
     * netty-socketio服务器
     */
    @Bean
    public SocketIOServer socketIOServer(WebSocketConfig webSocketConfig) {
        assert webSocketConfig != null : "socketio config not found";
        SocketIOServer server = new SocketIOServer(webSocketConfig.getSocketio());
        return server;
    }

    /**
     * 用于扫描netty-socketio的注解，比如 @OnConnect、@OnEvent
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }

    /**
     * 接受push组件发送的推流事件
     */
    @Bean
    public ApplicationListener<MessagePushEvent> socketIoMessagePushEvent() {
        final MessageTransportService messageTransportService = new MessageTransportServiceSocketIoImpl();
        return event -> {
            messageTransportService.pushToWebsite((MessageVo) event.getSource());
        };
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> startSocketIOServerListener(SocketIOServer socketIOServer, WebSocketConfig webSocketConfig) {
        return contextReadyEvent -> {
            log.info("SocketIOServer start...");
            socketIOServer.start();
            com.corundumstudio.socketio.Configuration socketio = webSocketConfig.getSocketio();
            log.info("SocketIOServer started: bindIp={}, port={}", socketio.getHostname(), socketio.getPort());
        };
    }

    @Bean
    ApplicationListener<ContextClosedEvent> stopSocketIOServerListener(SocketIOServer socketIOServer) {
        return contextClosedEvent -> {
            log.info("SocketIOServer stop...");
            socketIOServer.stop();
        };
    }
}
