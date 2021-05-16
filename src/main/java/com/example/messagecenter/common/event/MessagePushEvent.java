package com.example.messagecenter.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * 消息推送事件
 *
 * @author lixiangqian
 * @since 2021/5/16 13:18
 */
public class MessagePushEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public MessagePushEvent(Object source) {
        super(source);
    }
}
