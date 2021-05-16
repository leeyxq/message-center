package com.example.messagecenter.receive.service;

import com.example.messagecenter.common.vo.MessageVo;

/**
 * 消息接受服务
 *
 * @author lixiangqian
 * @since 2021/5/16 12:27
 */
public interface ReceiveMessageService {

    void sendMessage(MessageVo messageVo);
}