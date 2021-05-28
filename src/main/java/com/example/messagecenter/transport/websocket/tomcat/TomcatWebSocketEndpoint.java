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
import java.util.Iterator;
import java.util.Set;
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
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    @Getter
    private final static ConcurrentHashMap<String, Set<TomcatWebSocketEndpoint>> webSocketSet = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;
    /**
     * 标识当前连接客户端的用户名
     */
    private String name;

    /**
     * 指定发送
     *
     * @param name
     * @param message
     */
    public static void toUser(String name, String message) {
        Set<TomcatWebSocketEndpoint> endpoints = webSocketSet.get(name);
        if (endpoints == null) {
            log.warn("User {} not found, skipping sending message", name);
            return;
        }
        if (endpoints.isEmpty()) {
            log.warn("User {} not found, skipping sending message", name);
            webSocketSet.remove(name);
            return;
        }
        Iterator<TomcatWebSocketEndpoint> iterator = endpoints.iterator();
        while (iterator.hasNext()) {
            var endpoint = iterator.next();
            try {
                if (endpoint.session == null || !endpoint.session.isOpen()) {
                    log.warn("Session is empty or closed, skipping sending message");
                    iterator.remove();
                    continue;
                }
                endpoint.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("toUser err: name={}, msg={}, err={}", name, message, e.getMessage());
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "name") String name) {
        this.session = session;
        this.name = name;
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        Set<TomcatWebSocketEndpoint> tomcatWebSocketEndpoints = webSocketSet.get(name);
        if (tomcatWebSocketEndpoints == null) {
            synchronized (TomcatWebSocketEndpoint.class) {
                if (tomcatWebSocketEndpoints == null) {
                    tomcatWebSocketEndpoints = ConcurrentHashMap.newKeySet();
                }
            }
        }
        tomcatWebSocketEndpoints.add(this);
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

}