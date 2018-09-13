package com.common.redis;

public interface DistributedLock {

    /**
     * 获取锁
     * @author yangwenkui
     * @time 2016年5月6日 上午11:02:54
     * @return
     * @throws InterruptedException
     */
    public boolean acquire();

    /**
     * 释放锁
     * @author yangwenkui
     * @time 2016年5月6日 上午11:02:59
     */
    public void release();

}