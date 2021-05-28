package com.example.messagecenter.push.cluster.redis;

import com.example.messagecenter.common.event.MessageApplicationEventPublisher;
import com.example.messagecenter.push.cluster.redis.listener.MessagePushClusterRedisListener;
import com.example.messagecenter.push.cluster.redis.listener.MessageSendClusterRedisListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ErrorHandler;

import java.util.concurrent.Executor;

import static com.example.messagecenter.common.constant.CommonConst.*;
import static org.springframework.data.redis.listener.adapter.MessageListenerAdapter.ORIGINAL_DEFAULT_LISTENER_METHOD;

/**
 * 消息推送服务-集群redis实现
 *
 * @author lixiangqian
 * @since 2021/5/16 12:56
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = RUN_MODEL_CONFIG, havingValue = RUN_MODEL_CLUSTER_REDIS)
public class PushServiceClusterRedisConfig {
    /**
     * 订阅spring event事件-推送消息
     */
    @Bean
    public MessageSendClusterRedisListener messageSendClusterRedisListener(StringRedisTemplate stringRedisTemplate) {
        return new MessageSendClusterRedisListener(stringRedisTemplate);
    }

    /**
     * 订阅redis接受消息事件-用来推流
     */
    @Bean
    public MessagePushClusterRedisListener messagePushClusterRedisListener(MessageApplicationEventPublisher messageApplicationEventPublisher) {
        return new MessagePushClusterRedisListener(messageApplicationEventPublisher);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MessagePushClusterRedisListener listener) {
        return new MessageListenerAdapter(listener, ORIGINAL_DEFAULT_LISTENER_METHOD);
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter, Executor redisTaskExecutor, ErrorHandler errorHandler) {

        var container = new RedisMessageListenerContainer();
        container.setTaskExecutor(redisTaskExecutor);
        container.setConnectionFactory(connectionFactory);
        container.setErrorHandler(errorHandler);
        container.addMessageListener(listenerAdapter, new ChannelTopic(REDIS_CHANNEL_SENG_SMG));
        return container;
    }

    @Bean
    @ConfigurationProperties(prefix = "redis-subscribe-executor")
    public Executor redisTaskExecutor() {
        var taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("redis-subscribe");
        taskExecutor.setRejectedExecutionHandler((r, executor) -> log.error("redis-subscribe rejectedExecution: {}", r));
        return taskExecutor;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return t -> log.error("Unexpected error occurred in handle redis message.", t);
    }
}
