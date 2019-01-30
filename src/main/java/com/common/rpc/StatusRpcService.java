package com.common.rpc;

import com.common.util.RPCResult;

/**
 * rpc服务监存活状态
 */
public interface StatusRpcService {
    /**
     * 存活状态
     * @return
     */
    RPCResult<Boolean> status();
}
