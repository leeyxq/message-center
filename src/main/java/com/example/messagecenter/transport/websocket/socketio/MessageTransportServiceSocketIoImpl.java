package com.example.messagecenter.transport.websocket.socketio;

import com.example.messagecenter.common.vo.MessageVo;
import com.example.messagecenter.transport.MessageTransportService;
import com.example.messagecenter.transport.websocket.tomcat.TomcatWebSocketEndpoint;

/**
 * @author lixiangqian
 * @since 2021/5/16 14:58
 */
public class MessageTransportServiceSocketIoImpl implements MessageTransportService {
    @Override
    public void pushToWebsite(MessageVo messageVo) {
        MessageEventHandler.toUser(messageVo.getToUser(), messageVo.getMsg());
    }
}