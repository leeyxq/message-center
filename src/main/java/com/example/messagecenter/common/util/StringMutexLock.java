package com.example.messagecenter.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * 基于字符串互斥锁
 *
 * @author lixiangqian
 * @date 2021/5/28 5:02 下午
 **/
@Slf4j
@UtilityClass
public class StringMutexLock {
    /**
     * 字符锁管理器，将String转换为CountDownLatch
     * 即锁只会发生在真正有并发更新并且是同一个String的情况下
     */
    private static final ConcurrentMap<String, CountDownLatch> lockKeyHolder = new ConcurrentHashMap<>();

    /**
     * 锁结果返回（使用JDK中try-with-resources机制来自动释放锁）
     */
    @AllArgsConstructor
    @Getter
    public static class LockResult implements AutoCloseable {
        private String lockKey;

        @Override
        public void close() {
            unlock(lockKey);
        }
    }

    /**
     * 基于lockKey上锁，同步
     */

    public static LockResult lock(String lockKey) {
        while (!tryLock(lockKey)) {
            try {
                log.debug("Character lock concurrent update lock upgrade,{}", lockKey);
                blockOnSecondLevelLock(lockKey);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Character lock interrupt exception,{}", lockKey, e);
                break;
            }
        }
        return new LockResult(lockKey);
    }

    /**
     * 释放lockKey锁
     */
    private static boolean unlock(String lockKey) {
        CountDownLatch realLock = getAndReleaseLock1(lockKey);
        releaseSecondLevelLock(realLock);
        return true;
    }

    /**
     * 尝试给lockKey上锁
     */
    private static boolean tryLock(String lockKey) {
        return lockKeyHolder.putIfAbsent(lockKey, new CountDownLatch(1)) == null;
    }

    /**
     * 释放1级锁，并返回重量级锁
     */
    private static CountDownLatch getAndReleaseLock1(String lockKey) {
        return lockKeyHolder.remove(lockKey);
    }

    /**
     * 二级锁锁定(锁升级)
     */
    private static void blockOnSecondLevelLock(String lockKey) throws InterruptedException {
        CountDownLatch realLock = getRealLockByKey(lockKey);
        if (null != realLock) {
            realLock.await();
        }
    }

    /**
     * 二级锁解锁
     */
    private static void releaseSecondLevelLock(CountDownLatch realLock) {
        realLock.countDown();
    }

    /**
     * 通过lockKey获取对应锁实例
     */
    private static CountDownLatch getRealLockByKey(String lockKey) {
        return lockKeyHolder.get(lockKey);
    }
}
