package com.example.messagecenter.transport;

import com.example.messagecenter.common.vo.MessageVo;

/**
 * 消息传输服务
 *
 * @author lixiangqian
 * @since 2021/5/16 13:10
 */
public interface MessageTransportService {

    void pushToWebsite(MessageVo messageVo);
}
