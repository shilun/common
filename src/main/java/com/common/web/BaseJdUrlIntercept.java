//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web;

import com.common.web.url.JdUrl;
import com.common.web.url.JdUrlIntercept;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseJdUrlIntercept implements JdUrlIntercept {
    private static final Log log = LogFactory.getLog(BaseJdUrlIntercept.class);
    protected Map<String, String[]> urlMaps = new HashMap();
    protected String urlSeparate = "-";
    protected String urlSuffix = ".html";

    public BaseJdUrlIntercept() {
    }

    public void doIntercept(JdUrl jdUrl) {
        String path = jdUrl.getPath();
        if(StringUtils.isNotBlank(path) && this.urlMaps.containsKey(path)) {
            Object o = this.urlMaps.get(path);
            int start = path.lastIndexOf(46);
            int start1 = path.lastIndexOf(47);
            StringBuilder builder;
            if(start > start1) {
                builder = new StringBuilder(path.substring(0, start));
            } else {
                builder = new StringBuilder(path);
            }

            if(o != null) {
                String[] parameters = (String[])((String[])o);
                Map queryMap = jdUrl.getQuery();
                String[] var9 = parameters;
                int var10 = parameters.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    String parameter = var9[var11];
                    builder.append(this.urlSeparate);
                    if(StringUtils.isNotEmpty(parameter)) {
                        Object o1 = queryMap.get(parameter);
                        if(o1 != null) {
                            builder.append(jdUrl.encodeUrl(o1.toString()));
                        }
                    }

                    queryMap.remove(parameter);
                }
            }

            builder.append(this.urlSuffix);
            jdUrl.setPath(builder.toString());
        }

    }

    public void setUrlMaps(Map<String, String[]> urlMaps) {
        this.urlMaps = urlMaps;
    }

    public void setUrlSeparate(String urlSeparate) {
        this.urlSeparate = urlSeparate;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }
}
