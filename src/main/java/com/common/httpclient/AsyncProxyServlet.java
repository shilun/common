package com.common.httpclient;

import javax.servlet.Servlet;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.mortbay.io.Buffer;
import org.mortbay.jetty.HttpURI;
import org.mortbay.jetty.client.HttpClient;
import org.mortbay.jetty.client.HttpExchange;
import org.mortbay.util.IO;
import org.mortbay.util.ajax.Continuation;
import org.mortbay.util.ajax.ContinuationSupport;

public class AsyncProxyServlet implements Servlet {
    HttpClient _client;
    protected HashSet<String> _DontProxyHeaders = new HashSet();
    private ServletConfig config;
    private ServletContext context;

    public AsyncProxyServlet() {
        this._DontProxyHeaders.add("proxy-connection");
        this._DontProxyHeaders.add("connection");
        this._DontProxyHeaders.add("keep-alive");
        this._DontProxyHeaders.add("transfer-encoding");
        this._DontProxyHeaders.add("te");
        this._DontProxyHeaders.add("trailer");
        this._DontProxyHeaders.add("proxy-authorization");
        this._DontProxyHeaders.add("proxy-authenticate");
        this._DontProxyHeaders.add("upgrade");
    }

    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        this.context = config.getServletContext();
        this._client = new HttpClient();
        this._client.setConnectorType(2);

        try {
            this._client.start();
        } catch (Exception var3) {
            throw new ServletException(var3);
        }
    }

    public ServletConfig getServletConfig() {
        return this.config;
    }

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        while(res instanceof HttpServletResponseWrapper) {
            res = (HttpServletResponse)((HttpServletResponseWrapper)res).getResponse();
        }

        HttpServletRequest request = (HttpServletRequest)req;
        final HttpServletResponse response = (HttpServletResponse)res;
        if("CONNECT".equalsIgnoreCase(request.getMethod())) {
            this.handleConnect(request, response);
        } else {
            InputStream in = request.getInputStream();
            final OutputStream out = response.getOutputStream();
            final Continuation continuation = ContinuationSupport.getContinuation(request, request);
            if(!continuation.isPending()) {
                final byte[] buffer = new byte[4096];
                String uri = request.getRequestURI();
                if(request.getQueryString() != null) {
                    uri = uri + "?" + request.getQueryString();
                }

                HttpURI url = this.proxyHttpURI(request.getScheme(), request.getServerName(), request.getServerPort(), uri);
                if(url == null) {
                    response.sendError(403);
                    return;
                }

                HttpExchange exchange = new HttpExchange() {
                    protected void onRequestCommitted() throws IOException {
                    }

                    protected void onRequestComplete() throws IOException {
                    }

                    protected void onResponseComplete() throws IOException {
                        continuation.resume();
                    }

                    protected void onResponseContent(Buffer content) throws IOException {
                        while(content.hasContent()) {
                            int len = content.get(buffer, 0, buffer.length);
                            out.write(buffer, 0, len);
                        }

                    }

                    protected void onResponseHeaderComplete() throws IOException {
                    }

                    protected void onResponseStatus(Buffer version, int status, Buffer reason) throws IOException {
                        if(reason != null && reason.length() > 0) {
                            response.setStatus(status, reason.toString());
                        } else {
                            response.setStatus(status);
                        }

                    }

                    protected void onResponseHeader(Buffer name, Buffer value) throws IOException {
                        String s = name.toString().toLowerCase();
                        if(!AsyncProxyServlet.this._DontProxyHeaders.contains(s)) {
                            response.addHeader(name.toString(), value.toString());
                        }

                    }
                };
                exchange.setVersion(request.getProtocol());
                exchange.setMethod(request.getMethod());
                exchange.setURL(url.toString());
                String connectionHdr = request.getHeader("Connection");
                if(connectionHdr != null) {
                    connectionHdr = connectionHdr.toLowerCase();
                    if(connectionHdr.indexOf("keep-alive") < 0 && connectionHdr.indexOf("close") < 0) {
                        connectionHdr = null;
                    }
                }

                boolean xForwardedFor = false;
                boolean hasContent = false;
                long contentLength = -1L;
                Enumeration enm = request.getHeaderNames();

                while(true) {
                    String hdr;
                    String lhdr;
                    do {
                        do {
                            if(!enm.hasMoreElements()) {
                                exchange.setRequestHeader("Via", "1.1 (jetty)");
                                if(!xForwardedFor) {
                                    exchange.addRequestHeader("X-Forwarded-For", request.getRemoteAddr());
                                }

                                if(hasContent) {
                                    exchange.setRequestContentSource(in);
                                }

                                this._client.send(exchange);
                                continuation.suspend(30000L);
                                return;
                            }

                            hdr = (String)enm.nextElement();
                            lhdr = hdr.toLowerCase();
                        } while(this._DontProxyHeaders.contains(lhdr));
                    } while(connectionHdr != null && connectionHdr.indexOf(lhdr) >= 0);

                    if("content-type".equals(lhdr)) {
                        hasContent = true;
                    }

                    if("content-length".equals(lhdr)) {
                        contentLength = (long)request.getContentLength();
                    }

                    Enumeration vals = request.getHeaders(hdr);

                    while(vals.hasMoreElements()) {
                        String val = (String)vals.nextElement();
                        if(val != null) {
                            exchange.setRequestHeader(lhdr, val);
                            xForwardedFor |= "X-Forwarded-For".equalsIgnoreCase(hdr);
                        }
                    }
                }
            }
        }

    }

    protected HttpURI proxyHttpURI(String scheme, String serverName, int serverPort, String uri) throws MalformedURLException {
        return new HttpURI(scheme + "://" + serverName + ":" + serverPort + uri);
    }

    public void handleConnect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String port = "";
        String host = "";
        int c = uri.indexOf(58);
        if(c >= 0) {
            port = uri.substring(c + 1);
            host = uri.substring(0, c);
            if(host.indexOf(47) > 0) {
                host = host.substring(host.indexOf(47) + 1);
            }
        }

        InetSocketAddress inetAddress = new InetSocketAddress(host, Integer.parseInt(port));
        InputStream in = request.getInputStream();
        OutputStream out = response.getOutputStream();
        Socket socket = new Socket(inetAddress.getAddress(), inetAddress.getPort());
        response.setStatus(200);
        response.setHeader("Connection", "close");
        response.flushBuffer();
        IO.copyThread(socket.getInputStream(), out);
        IO.copy(in, socket.getOutputStream());
    }

    public String getServletInfo() {
        return "Proxy Servlet";
    }

    public void destroy() {
    }

    public static class Transparent extends AsyncProxyServlet {
        String _prefix;
        String _proxyTo;
        public Transparent() {

        }

        public Transparent(String proxyTo,String prefix) {
            this._proxyTo=proxyTo;
            this._prefix = prefix;
        }

        public Transparent(String prefix, String server, int port) {
            this._prefix = prefix;
            this._proxyTo = "http://" + server + ":" + port;
        }

        public void init(ServletConfig config) throws ServletException {
            if(config.getInitParameter("ProxyTo") != null) {
                this._proxyTo = config.getInitParameter("ProxyTo");
            }

            if(config.getInitParameter("Prefix") != null) {
                this._prefix = config.getInitParameter("Prefix");
            }

            if(this._proxyTo == null) {
                throw new UnavailableException("No ProxyTo");
            } else {
                super.init(config);
                config.getServletContext().log("Transparent AsyncProxyServlet @ " + (this._prefix == null?"-":this._prefix) + " to " + this._proxyTo);
            }
        }

        protected HttpURI proxyHttpURI(String scheme, String serverName, int serverPort, String uri) throws MalformedURLException {
            return this._prefix != null && !uri.startsWith(this._prefix)?null:(this._prefix != null?new HttpURI(this._proxyTo + uri.substring(this._prefix.length())):new HttpURI(this._proxyTo + uri));
        }
    }
}
