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
        toSession(webSocketSet.get(name), name, message);
    }

    private static void toSession(NettyWebSocketEndpoint nettyWebSocketEndpoint, String name, String message) {
        try {
            if (nettyWebSocketEndpoint == null) {
                log.warn("skip send: not found senderWebSocket");
                return;
            }
            if (nettyWebSocketEndpoint.session == null || !nettyWebSocketEndpoint.session.isOpen()) {
                log.warn("skip send: session is null or closed");
                webSocketSet.remove(name);
                return;
            }
            nettyWebSocketEndpoint.session.sendText(message);
        } catch (Exception e) {
            log.error("toSession err: name={}, msg={}, err={}", name, message, e.getMessage());
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
        log.info("[WebSocket] 连接成功，当前连接人数为：={}", webSocketSet.size());
    }

    @OnClose
    public void onClose(Session session) {
        log.debug("one connection closed: sessionId={}, name={}", session.id(), name);
        webSocketSet.remove(this.name);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}", webSocketSet.size());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("onError: sessionId=" + session.id(), throwable);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.debug("onMessage: {}", message);
        session.sendText("Hello Netty!");
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
