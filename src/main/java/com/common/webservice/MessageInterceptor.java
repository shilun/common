//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.webservice;

import com.common.util.DESEncryptUtils;
import com.common.util.DateUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import com.common.util.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

public class MessageInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
    public static final String xml_authToken = "auth:token";
    private static final Logger LOGGER = Logger.getLogger(MessageInterceptor.class);
    private SAAJInInterceptor saa = new SAAJInInterceptor();
    public static final int CACHE_LOGIN_TIME = 1800;
    private String authKey = "26115be0be4d4d5caa1fb7cc067642fx";

    public MessageInterceptor() {
        super("pre-protocol");
        this.getAfter().add(SAAJInInterceptor.class.getName());
    }

    public void handleMessage(SoapMessage message) throws Fault {
        List headers = message.getHeaders();
        String content = null;
        SOAPMessage mess = (SOAPMessage) message.getContent(SOAPMessage.class);
        if (mess == null) {
            this.saa.handleMessage(message);
            mess = (SOAPMessage) message.getContent(SOAPMessage.class);
        }

        SOAPHeader head = null;

        try {
            head = mess.getSOAPHeader();
        } catch (SOAPException var14) {
            LOGGER.error("webservice audi error");
        }

        if (head == null) {
            throw new Fault(new SOAPException("web service audi error"));
        } else {
            try {
                NodeList decrypt = head.getElementsByTagName("tns:token");
                content = decrypt.item(0).getTextContent();
            } catch (Exception var13) {
                SOAPException data = new SOAPException("认证错误");
                throw new Fault(data);
            }
            if (StringUtils.isNotBlank(content) && content.equals(authKey)) {
                LOGGER.info("webservice call success");
                return;
            }
            throw new Fault(new SOAPException("web service audi error  authKey "));

        }
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
