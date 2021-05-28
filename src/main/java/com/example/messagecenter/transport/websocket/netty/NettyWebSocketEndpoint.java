package com.example.messagecenter.transport.websocket.netty;


import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty端点定义
 *
 * @author lixiangqian
 * @date 2021/5/17 10:38 上午
 **/
@Slf4j
@ToString
@EqualsAndHashCode
@ConditionalOnClass(ServerEndpoint.class)
@ServerEndpoint(path = "${ws.netty.context}", host = "${ws.netty.host}", port = "${ws.netty.port}", bossLoopGroupThreads = "${ws.netty.bossThreads}", workerLoopGroupThreads = "${ws.netty.workerThreads}")
public class NettyWebSocketEndpoint {
    /**
     * 用户和websocket连接缓存
     */
    @Getter
    private static final ConcurrentHashMap<String, Set<NettyWebSocketEndpoint>> USER_AND_WEB_SOCKET_CACHE = new ConcurrentHashMap<>();

    private Session session;
    private String name;

    /**
     * 指定用户发送
     */
    public static void toUser(String name, String message) {
        var endpoints = USER_AND_WEB_SOCKET_CACHE.get(name);
        if (endpoints == null) {
            log.warn("User {} not found, skipping sending message", name);
            return;
        }
        if (endpoints.isEmpty()) {
            log.warn("User {} not found, skipping sending message", name);
            USER_AND_WEB_SOCKET_CACHE.remove(name);
            return;
        }
        Iterator<NettyWebSocketEndpoint> iterator = endpoints.iterator();
        while (iterator.hasNext()) {
            var endpoint = iterator.next();
            try {
                if (endpoint.session == null || !endpoint.session.isOpen()) {
                    log.warn("Session is empty or closed, skipping sending message");
                    iterator.remove();
                    continue;
                }
                endpoint.session.sendText(message);
            } catch (Exception e) {
                log.error("toUser err: name={}, msg={}, err={}", name, message, e.getMessage());
            }
        }
    }


    @BeforeHandshake
    public void handshake(Session session, HttpHeaders headers, @RequestParam String name, @RequestParam MultiValueMap<String, Object> reqMap, @PathVariable String arg, @PathVariable Map<String, Object> pathMap) {
        session.setSubprotocols("stomp");
    }

    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @RequestParam String name, @RequestParam MultiValueMap<String, Object> reqMap, @PathVariable String arg, @PathVariable Map<String, Object> pathMap) {
        log.debug("new connection: name={}", name);
        this.session = session;
        this.name = name;
        // 1.find all endpoints for the user by user name
        var nettyWebSocketEndpoints = USER_AND_WEB_SOCKET_CACHE.computeIfAbsent(name, k -> ConcurrentHashMap.newKeySet());

        nettyWebSocketEndpoints.add(this);
        log.info("connection successful,number of connections：={}", USER_AND_WEB_SOCKET_CACHE.size());
    }

    @OnClose
    public void onClose(Session session) {
        log.debug("one connection closed: sessionId={}, name={}", session.id(), name);
        var webSocketEndpoints = USER_AND_WEB_SOCKET_CACHE.get(this.name);
        if (webSocketEndpoints != null) {
            webSocketEndpoints.remove(this);
        }
        log.debug("one connection closed: number of connections={}", USER_AND_WEB_SOCKET_CACHE.size());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("onError: sessionId=" + session.id(), throwable);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.debug("new message: {}", message);
        session.sendText("server response: " + message);
    }

    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        log.debug("onBinary: {}", new String(bytes));
        session.sendBinary(bytes);
    }

    @OnEvent
    public void onEvent(Session session, Object evt) {
        if (evt instanceof IdleStateEvent) {
            var idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    log.debug("read idle: sessionId={}", session.id());
                    break;
                case WRITER_IDLE:
                    log.debug("write idle: sessionId={}", session.id());
                    break;
                case ALL_IDLE:
                    log.debug("all idle: sessionId={}", session.id());
                    break;
                default:
                    break;
            }
        }
    }
}
