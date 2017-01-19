//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.webservice;

import com.common.util.DESEncryptUtils;
import com.common.util.DateUtil;
import java.util.Calendar;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AuthHeader extends AbstractSoapInterceptor {
    public static final String xml_namespaceUR_att = "http://webservice/authentication";
    public static final String xml_authToken = "tns:token";
    private String qname;
    private String key = "26115be0be4d4d5caa1fb7cc067642fx";

    public AuthHeader() {
        super("write");
    }

    public void handleMessage(SoapMessage message) throws Fault {
        QName qname = new QName("RequestSOAPHeader");
        Document doc = DOMUtils.createDocument();
        Element root = doc.createElementNS("http://webservice/authentication", "tns:RequestSOAPHeader");
        Element token = doc.createElement("tns:token");
        token.setTextContent(this.getContent());
        root.appendChild(token);
        SoapHeader head = new SoapHeader(qname, root);
        List headers = message.getHeaders();
        headers.add(head);
    }

    public String getKey() {
        return this.key;
    }

    private String getContent() {
        return DESEncryptUtils.encrypt(this.key + "_" + DateUtil.format(Calendar.getInstance().getTime()), this.key);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQname() {
        return this.qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }
}
