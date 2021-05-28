package com.example.messagecenter.transport.websocket.netty;


import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty端点定义
 *
 * @author lixiangqian
 * @date 2021/5/17 10:38 上午
 **/
@Slf4j
@ConditionalOnClass(ServerEndpoint.class)
@ServerEndpoint(path = "${ws.netty.context}", host = "${ws.netty.host}", port = "${ws.netty.port}", bossLoopGroupThreads = "${ws.netty.bossThreads}", workerLoopGroupThreads = "${ws.netty.workerThreads}")
public class NettyWebSocketEndpoint {
    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    @Getter
    private static ConcurrentHashMap<String, NettyWebSocketEndpoint> webSocketSet = new ConcurrentHashMap<>();
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
        var endpoint = webSocketSet.get(name);
        if (endpoint == null) {
            log.warn("User {} not found, skipping sending message", name);
            return;
        }
        try {
            if (endpoint.session == null || !endpoint.session.isOpen()) {
                log.warn("Session is empty or closed, skipping sending message");
                return;
            }
            endpoint.session.sendText(message);
        } catch (Exception e) {
            log.error("toUser err: name={}, msg={}, err={}", name, message, e.getMessage());
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
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        webSocketSet.put(name, this);
        log.info("connection successful,number of connections：={}", webSocketSet.size());
    }

    @OnClose
    public void onClose(Session session) {
        log.debug("one connection closed: sessionId={}, name={}", session.id(), name);
        webSocketSet.remove(this.name);
        log.info("connection lost,number of connections：={}", webSocketSet.size());
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
