package com.common.health;

import com.common.rpc.StatusRpcService;
import com.common.util.RPCResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public abstract class AbstractHealThIndicator implements HealthIndicator {
    private final static Logger logger = LoggerFactory.getLogger(AbstractHealThIndicator.class);

    @Override
    public Health health() {
        try {
            RPCResult<Boolean> status = getStatusRpcService().status();
            if (status.getSuccess()) {
                return new Health.Builder().withDetail("status", "UP").up().build();
            }
        } catch (Exception e) {
            logger.error(this.getClass().getSimpleName() + " 监控健康检查失败");
        }
        return new Health.Builder().withDetail("status", "DOWN").up().build();
    }

    /**
     * 返回抽像服务RPC接口
     *
     * @return
     */
    protected abstract StatusRpcService getStatusRpcService();


}
