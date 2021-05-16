package com.example.messagecenter.transport.websocket.tomcat;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lixiangqian
 * @since 2021/5/15 18:28
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{name}")
@ConditionalOnClass(ServerEndpointExporter.class)
public class TomcatWebSocketEndpoint {

    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    /**
     * 标识当前连接客户端的用户名
     */
    private String name;

    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    @Getter
    private static ConcurrentHashMap<String, TomcatWebSocketEndpoint> webSocketSet = new ConcurrentHashMap<>();


    @OnOpen
    public void onOpen(Session session, @PathParam(value = "name") String name) {
        this.session = session;
        this.name = name;
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        webSocketSet.put(name, this);
        log.info("[WebSocket] 连接成功，当前连接人数为：={}", webSocketSet.size());
    }


    @OnClose
    public void onClose() {
        webSocketSet.remove(this.name);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}", webSocketSet.size());
    }

    @OnMessage
    public void anMessage(String message) {
        log.info("[WebSocket] 收到消息：{}", message);
    }

    /**
     * 指定发送
     *
     * @param name
     * @param message
     */
    public static void toUser(String name, String message) {
        toSession(webSocketSet.get(name), name, message);
    }

    private static void toSession(TomcatWebSocketEndpoint tomcatWebSocketEndpoint, String name, String message) {
        try {
            if (tomcatWebSocketEndpoint == null) {
                log.warn("skip send: not found senderWebSocket");
                return;
            }
            if (tomcatWebSocketEndpoint.session == null || !tomcatWebSocketEndpoint.session.isOpen()) {
                log.warn("skip send: session is null or closed");
                webSocketSet.remove(name);
                return;
            }
            tomcatWebSocketEndpoint.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("toSession err: name={}, msg={}, err={}", name, message, e.getMessage());
        }
    }

}