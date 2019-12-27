package com.common.rpc;

import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import org.springframework.data.domain.Page;

import java.util.List;

public abstract class StatusRpcServiceImpl implements StatusRpcService {
    @Override
    public RPCResult<Boolean> status() {
        RPCResult<Boolean> result = new RPCResult<>();
        result.setSuccess(true);
        return result;
    }

    /**
     * 对象转换
     * @param typeClass 目标类型
     * @param source 源类型
     * @param <T>
     * @return
     */
    protected <T> T clone(Class<T> typeClass,Object source){
        return BeanCoper.clone(typeClass, source);
    }

    /**
     * list数据转换
     * @param typeClass 目标类型
     * @param sourcepage 源类型
     * @param <T>
     * @return
     */
    protected <T> List<T> clone(Class<T> typeClass, List<?> sourcepage){
        return BeanCoper.clone(typeClass, sourcepage);
    }
    /**
     * 分页数据转换
     * @param typeClass 目标类型
     * @param sourcepage 源类型
     * @param <T>
     * @return
     */
    protected <T> Page<T> clone(Class<T> typeClass, Page<?> sourcepage){
        return BeanCoper.clone(typeClass, sourcepage);
    }
}
