//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.springmvc.velocity;

import com.common.springmvc.velocity.SpringVelocityLayoutView;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

public class SpringVelocityLayoutViewResolver extends VelocityLayoutViewResolver {
    private Map<String, Object> velocityTools;

    public SpringVelocityLayoutViewResolver() {
    }

    protected Class requiredViewClass() {
        return SpringVelocityLayoutView.class;
    }

    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        SpringVelocityLayoutView view = (SpringVelocityLayoutView)super.buildView(viewName);
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        this.velocityTools.put("root", request.getContextPath());
        if(this.velocityTools != null && this.velocityTools.size() > 0) {
            view.setVelocityTools(this.velocityTools);
        }

        return view;
    }

    public void setVelocityTools(Map<String, Object> velocityTools) {
        this.velocityTools = velocityTools;
    }
}
