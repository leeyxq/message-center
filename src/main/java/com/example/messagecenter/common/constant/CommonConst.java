package com.example.messagecenter.common.constant;

import lombok.experimental.UtilityClass;

/**
 * @author lixiangqian
 * @since 2021/5/15 23:59
 */
@UtilityClass
public class CommonConst {

    public static final String REDIS_CHANNEL_SENG_SMG = "ws_send_msg";

    /**
     * 运行模式配置
     */
    public static final String RUN_MODEL_CONFIG = "application.run-model";
    public static final String RUN_MODEL_SINGLE = "single";
    public static final String RUN_MODEL_CLUSTER_REDIS = "cluster-redis";
}
