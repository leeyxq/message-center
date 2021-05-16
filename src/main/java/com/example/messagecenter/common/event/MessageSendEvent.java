package com.example.messagecenter.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * 消息发送事件
 *
 * @author lixiangqian
 * @since 2021/5/16 13:17
 */
public class MessageSendEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public MessageSendEvent(Object source) {
        super(source);
    }
}
