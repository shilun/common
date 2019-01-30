package com.common.rpc;

import com.common.util.RPCResult;

public abstract class StatusRpcServiceImpl implements StatusRpcService {
    @Override
    public RPCResult<Boolean> status() {
        RPCResult<Boolean> result = new RPCResult<>();
        result.setSuccess(true);
        return result;
    }
}
