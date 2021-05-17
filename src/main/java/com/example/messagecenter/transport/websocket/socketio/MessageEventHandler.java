package com.example.messagecenter.transport.websocket.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 消息处理类
 *
 * @author lixiangqian
 * @since 2021/5/16 15:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnClass(SocketIOServer.class)
public class MessageEventHandler {

    public static ConcurrentMap<String, SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

    /**
     * 客户端连接的时候触发
     *
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        String name = client.getHandshakeData().getSingleUrlParam("name");
        //存储SocketIOClient，用于发送消息
        socketIOClientMap.put(name, client);
        //回发消息
        client.sendEvent("message", "onConnect back");
        log.info("客户端:" + client.getSessionId() + "已连接,mac=" + name);
    }

    /**
     * 客户端关闭连接时触发
     *
     * @param client
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("客户端:" + client.getSessionId() + "断开连接");
    }

    /**
     * 客户端事件
     *
     * @param client  　客户端信息
     * @param request 请求信息
     * @param data    　客户端发送数据
     */
    @OnEvent(value = "send")
    public void onEvent(SocketIOClient client, AckRequest request, Object data) {
        log.info("发来消息：" + data);
        //回发消息
        client.sendEvent("message", data);
    }

    @OnEvent(value = "login")
    public void onEventLogin(SocketIOClient client, AckRequest request, Object data) {
        log.info("login event：" + data);
        //回发消息
        client.sendEvent("message", data);
    }

    public static void toUser(String name, String msg) {
        SocketIOClient socketIOClient = socketIOClientMap.get(name);
        if (socketIOClient == null) {
            log.debug("sentToUser error: not found user, user={}, msg={}", name, msg);
        }
        if (!socketIOClient.isChannelOpen()) {
            log.debug("sentToUser error: connection lost, user={}, msg={}", name, msg);
        }
        socketIOClient.sendEvent("message", msg);
    }
}
