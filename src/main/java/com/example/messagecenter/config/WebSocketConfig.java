package com.example.messagecenter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lixiangqian
 * @since 2021/5/16 15:22
 */
@Data
@Component
@ConfigurationProperties(prefix = "ws")
public class WebSocketConfig {

    com.corundumstudio.socketio.Configuration socketio;
}
