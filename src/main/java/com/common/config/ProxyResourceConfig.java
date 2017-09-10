package com.common.config;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * resource
 *
 * @author shilun
 */
@Configuration
@Conditional(ProxyResourceCondition.class)
public class ProxyResourceConfig implements EnvironmentAware {
    @Bean
    @RefreshScope
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servletRegistrationBean = null;
        if (environment.getProperty("proxy.nodejs.servlet_url") != null) {
            servletRegistrationBean = new ServletRegistrationBean(new ProxyServlet(), environment.getProperty("proxy.nodejs.servlet_url"), "/static/*");
            servletRegistrationBean.addInitParameter("targetUri", environment.getProperty("proxy.nodejs.servlet_url"));
            servletRegistrationBean.addInitParameter(ProxyServlet.P_LOG, environment.getProperty("logging_enabled", "false"));
        } else {
            servletRegistrationBean = new ServletRegistrationBean(new TestServlet(), "/test/*", "/test/*");
        }
        return servletRegistrationBean;
    }

    @Bean
    @RefreshScope
    public ServletRegistrationBean staticServletBean() {
        ServletRegistrationBean servletRegistrationBean = null;
        if (environment.getProperty("app.static.path") != null) {
            servletRegistrationBean = new ServletRegistrationBean(new MyServlet(), "/static/*");
        } else {
            servletRegistrationBean = new ServletRegistrationBean(new TestServlet(), "/staticTest/*", "/staticTest/*");
        }
        return servletRegistrationBean;
    }

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private static List<String> picFileTypes = new ArrayList<>();

    static {
        picFileTypes.add(".jpg");
        picFileTypes.add(".png");
        picFileTypes.add(".gif");
    }

    class MyServlet extends HttpServlet {
        private static final long serialVersionUID = -8685285401859800066L;

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String rootProjectPath = (String) System.getProperties().get("user.dir");
            String currentProjectPath = environment.getProperty("app.static.path");
            String realPath = rootProjectPath + currentProjectPath + req.getPathInfo();
            if (req.getPathInfo().endsWith(".jpg") || req.getPathInfo().endsWith(".gif") || req.getPathInfo().endsWith(".png")) {
                resp.setContentType("image/jpeg");
            }
            if (req.getPathInfo().endsWith(".css")) {
                resp.setContentType("text/css");
            }
            if (req.getPathInfo().endsWith(".js")) {
                resp.setContentType("application/x-javascript");
            }
            if (req.getPathInfo().endsWith(".html")) {
                resp.setContentType("text/html");
            }
            OutputStream os = resp.getOutputStream();// 获得servlet的servletoutputstream对象
            byte[] buffer = new byte[2048];
            FileInputStream fos = new FileInputStream(realPath);// 打开图片文件
            int count;
            while ((count = fos.read(buffer)) > 0) {
                os.write(buffer, 0, count);
            }
            fos.close();
        }
    }
}