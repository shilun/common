//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web.interceptor.rpc;

import com.common.util.RPCResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ResourcesRPCService {
    /**
     * 查找运维权限资源
     * @param pin
     * @return
     */
    @RequestMapping("/rpc/resource/findResources")
    public RPCResult<List<String>> findResources(@RequestParam(name = "pin") String pin);
}
