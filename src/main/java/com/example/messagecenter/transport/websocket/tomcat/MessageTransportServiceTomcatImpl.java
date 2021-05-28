package com.example.messagecenter.transport.websocket.tomcat;

import com.example.messagecenter.common.vo.MessageVo;
import com.example.messagecenter.transport.service.MessageTransportService;

/**
 * @author lixiangqian
 * @since 2021/5/16 14:58
 */
public class MessageTransportServiceTomcatImpl implements MessageTransportService {
    @Override
    public void pushToWebsite(MessageVo messageVo) {
        TomcatWebSocketEndpoint.toUser(messageVo.getToUser(), messageVo.getMsg());
    }
}