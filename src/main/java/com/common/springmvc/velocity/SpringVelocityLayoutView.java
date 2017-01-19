//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.springmvc.velocity;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.context.Context;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;

public class SpringVelocityLayoutView extends VelocityLayoutView {
    private Map<String, Object> velocityTools;

    public SpringVelocityLayoutView() {
    }

    protected void doRender(Context context, HttpServletResponse response) throws Exception {
        this.merge(context, this.velocityTools);
        super.doRender(context, response);
    }

    private void merge(Context context, Map<String, Object> map) {
        if(map != null) {
            Iterator var3 = map.entrySet().iterator();

            while(var3.hasNext()) {
                Entry stringObjectEntry = (Entry)var3.next();
                context.put((String)stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        }

    }

    public void setVelocityTools(Map<String, Object> velocityTools) {
        this.velocityTools = velocityTools;
    }
}
