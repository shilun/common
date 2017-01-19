//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web.interceptor.rpc;

import com.common.util.Result;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ResourcesRPCService {
    /**
     * 查找运维权限资源
     * @param pin
     * @return
     */
    public Result<List<String>> findResources(@WebParam(name = "account") String pin);
}
