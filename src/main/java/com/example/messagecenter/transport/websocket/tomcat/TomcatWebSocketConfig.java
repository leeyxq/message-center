package com.example.messagecenter.transport.websocket.tomcat;

import com.example.messagecenter.common.event.MessagePushEvent;
import com.example.messagecenter.common.vo.MessageVo;
import com.example.messagecenter.transport.MessageTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author lixiangqian
 * @since 2021/5/15 20:21
 */

@Component
@ConditionalOnClass(ServerEndpointExporter.class)
public class TomcatWebSocketConfig {

    /**
     * ServerEndpointExporter 作用
     * <p>
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter tomcatServerEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 接受push组件发送的推流事件
     */
    @Bean
    public ApplicationListener<MessagePushEvent> tomcatMessagePushEvent(@Autowired @Qualifier("tomcatMessageTransportService") MessageTransportService messageTransportService) {
        return event -> messageTransportService.pushToWebsite((MessageVo) event.getSource());
    }

    @Bean("tomcatMessageTransportService")
    public MessageTransportService tomcatMessageTransportService() {
        return new MessageTransportServiceTomcatImpl();
    }
}