package com.example.messagecenter.transport.service;

import com.example.messagecenter.common.vo.MessageVo;

/**
 * 消息传输服务
 *
 * @author lixiangqian
 * @since 2021/5/16 13:10
 */
public interface MessageTransportService {

    /**
     * 发送消息
     *
     * @param messageVo 消息对象
     */
    void pushToWebsite(MessageVo messageVo);
}
