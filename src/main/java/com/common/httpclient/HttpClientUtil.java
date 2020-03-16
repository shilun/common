//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.httpclient;

import com.common.exception.ApplicationException;
import com.common.security.MD5;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
    private RequestConfig requestConfig;
    private String authKey;
    private Map<String, String> header = new HashMap();

    public void setTimeOut(Integer timeout) {
        this.requestConfig = RequestConfig.custom().setSocketTimeout(timeout.intValue()).setConnectTimeout(timeout.intValue()).setConnectionRequestTimeout(timeout.intValue()).build();
    }

    public HttpClientUtil() {
    }

    private String getHeader() {
        return StringUtils.isNotBlank(this.authKey) ? MD5.MD5Str(this.authKey) : null;
    }

    public void putHeader(String key, String value) {
        this.header.put(key, value);
    }

    public String sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);
        if (header.size() != 0) {
            for (String str : header.keySet()) {
                httpPost.setHeader(new BasicHeader(str, header.get(str)));
            }
        }

        return this.sendHttpPost(httpPost);
    }

    public String sendHttpPost(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);
        if (header.size() != 0) {
            for (String str : header.keySet()) {
                httpPost.setHeader(new BasicHeader(str, header.get(str)));
            }
        }

        try {
            StringEntity e = new StringEntity(params, "UTF-8");
            e.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(e);
        } catch (Exception var5) {
            LOGGER.error("sendHttpPost", var5);
            throw new ApplicationException("system.error");
        }

        return this.sendHttpPost(httpPost);
    }

    public String sendHttpPost(String httpUrl, Map<String, String> maps) {
        HttpPost httpPost = new HttpPost(httpUrl);
        if (header.size() != 0) {
            for (String str : header.keySet()) {
                httpPost.setHeader(new BasicHeader(str, header.get(str)));
            }
        }
        ArrayList nameValuePairs = new ArrayList();
        Iterator e = maps.keySet().iterator();

        while (e.hasNext()) {
            String key = (String) e.next();
            nameValuePairs.add(new BasicNameValuePair(key, (String) maps.get(key)));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception var7) {
            LOGGER.error("sendHttpPost", var7);
            throw new ApplicationException("system.error");
        }

        return this.sendHttpPost(httpPost);
    }

    /**
     * 发送postjson
     * @param url
     * @param body
     * @return
     */
    public String doPostJsonBody(String url,String body){
        HttpPost httpPost = new HttpPost(url);
        if (header.size() != 0) {
            for (String str : header.keySet()) {
                httpPost.setHeader(new BasicHeader(str, header.get(str)));
            }
        }
        StringEntity setEntity = new StringEntity(body,"utf-8");
        setEntity.setContentType("application/json");
        setEntity.setContentEncoding("UTF-8");
        httpPost.setEntity(setEntity);
        return sendHttpPost(httpPost);
    }

    public JSONObject doPostJson(String url, Map<String, String> params) {
        String content = this.sendHttpPost(url, params);
        return JSONObject.fromObject(content);
    }

    public JSONObject doSendJson(String url, Map<String, String> params) {
        String content = this.sendHttpPost(url, params);
        return JSONObject.fromObject(content);
    }

    public String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists) {
        HttpPost httpPost = new HttpPost(httpUrl);
        if (header.size() != 0) {
            for (String str : header.keySet()) {
                httpPost.setHeader(new BasicHeader(str, header.get(str)));
            }
        }

        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
        Iterator reqEntity = maps.keySet().iterator();

        while (reqEntity.hasNext()) {
            String file = (String) reqEntity.next();
            meBuilder.addPart(file, new StringBody((String) maps.get(file), ContentType.TEXT_PLAIN));
        }

        reqEntity = fileLists.iterator();

        while (reqEntity.hasNext()) {
            File file1 = (File) reqEntity.next();
            FileBody fileBody = new FileBody(file1);
            meBuilder.addPart("files", fileBody);
        }

        HttpEntity reqEntity1 = meBuilder.build();
        httpPost.setEntity(reqEntity1);
        return this.sendHttpPost(httpPost);
    }

    private String sendHttpPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;

        try {
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(this.requestConfig);
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception var15) {
            LOGGER.error("sendHttpPost", var15);
            throw new ApplicationException("system.error");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }

                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException var14) {
                LOGGER.error("sendHttpPost", var14);
            }

        }

        return responseContent;
    }

    public String sendHttpGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);
        if (this.authKey != null) {
            httpGet.setHeader(new BasicHeader("authKey", this.getHeader()));
        }

        return this.sendHttpGet(httpGet);
    }

    public String sendHttpsGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);
        return this.sendHttpsGet(httpGet);
    }

    private String sendHttpGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;

        try {
            httpClient = HttpClients.createDefault();
            httpGet.setConfig(this.requestConfig);
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception var15) {
            LOGGER.error("sendHttpGet", var15);
            throw new ApplicationException("system.error");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }

                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException var14) {
                LOGGER.error("sendHttpGet", var14);
            }

        }

        return responseContent;
    }

    private String sendHttpsGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;

        try {
            PublicSuffixMatcher e = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(e);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(this.requestConfig);
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception var16) {
            LOGGER.error("sendHttpsGet", var16);
            throw new ApplicationException("system.error");
        } finally {
            try {
                if (response != null) {
                    response.close();
                }

                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException var15) {
                LOGGER.error("sendHttpsGet", var15);
            }

        }

        return responseContent;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
        this.header.put("authKey", authKey);
    }
}
