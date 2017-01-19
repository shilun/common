//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web.url;

import com.common.web.url.JdUrl;
import java.util.Map;

public class JdUrlUtils {
    private Map<String, JdUrl> velocityUrl;

    public JdUrlUtils() {
    }

    public void setVelocityUrl(Map<String, JdUrl> velocityUrl) {
        this.velocityUrl = velocityUrl;
    }

    public JdUrl getJdUrl(String key) {
        JdUrl org = (JdUrl)this.velocityUrl.get(key);
        JdUrl jdUrl = org.clone();
        jdUrl.setJdUrl(org);
        return jdUrl;
    }
}
