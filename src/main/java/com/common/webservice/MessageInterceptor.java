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
        SOAPMessage mess = (SOAPMessage)message.getContent(SOAPMessage.class);
        if(mess == null) {
            this.saa.handleMessage(message);
            mess = (SOAPMessage)message.getContent(SOAPMessage.class);
        }

        SOAPHeader head = null;

        try {
            head = mess.getSOAPHeader();
        } catch (SOAPException var14) {
            LOGGER.error("webservice audi error");
        }

        if(head == null) {
            throw new Fault(new SOAPException("web service audi error"));
        } else {
            try {
                NodeList decrypt = head.getElementsByTagName("tns:token");
                content = decrypt.item(0).getTextContent();
            } catch (Exception var13) {
                SOAPException data = new SOAPException("认证错误");
                throw new Fault(data);
            }

            String decrypt1 = DESEncryptUtils.decrypt(content, this.authKey);
            String[] data1 = decrypt1.split("_");
            if(data1.length == 2 && data1[0].equals(this.authKey)) {
                Date remoteTime = null;

                try {
                    remoteTime = DateUtil.parseDateTime(data1[1]);
                } catch (ParseException var12) {
                    LOGGER.error("web service audi error:-> time format error", var12);
                    throw new Fault(new SOAPException("web service audi error:-> time format error"));
                }

                Date currentTime = Calendar.getInstance().getTime();
                Date start = DateUtils.addMinutes(currentTime, -5);
                Date end = DateUtils.addMinutes(currentTime, 5);
                if(remoteTime.after(start) && remoteTime.before(end)) {
                    LOGGER.info("webservice call success");
                } else {
                    LOGGER.error("currentTime " + DateUtil.format(currentTime));
                    LOGGER.error("remoteTime " + DateUtil.format(remoteTime));
                    LOGGER.error("web service audi error :->time is not synchronization");
                    throw new Fault(new SOAPException("web service audi error :->time is not synchronization"));
                }
            } else {
                throw new Fault(new SOAPException("web service audi error  authKey "));
            }
        }
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
