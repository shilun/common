//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

public class JaxbUtil {
    private JAXBContext jaxbContext;

    public JaxbUtil(Class... types) {
        try {
            this.jaxbContext = JAXBContext.newInstance(types);
        } catch (JAXBException var3) {
            throw new RuntimeException(var3);
        }
    }

    public String toXml(Object root, String encoding) {
        try {
            StringWriter e = new StringWriter();
            this.createMarshaller(encoding).marshal(root, e);
            return e.toString();
        } catch (JAXBException var4) {
            throw new RuntimeException(var4);
        }
    }

    public String toXml(Collection root, String rootName, String encoding) {
        try {
            JaxbUtil.CollectionWrapper e = new JaxbUtil.CollectionWrapper();
            e.collection = root;
            JAXBElement wrapperElement = new JAXBElement(new QName(rootName), JaxbUtil.CollectionWrapper.class, e);
            StringWriter writer = new StringWriter();
            this.createMarshaller(encoding).marshal(wrapperElement, writer);
            return writer.toString();
        } catch (JAXBException var7) {
            throw new RuntimeException(var7);
        }
    }

    public <T> T fromXml(String xml) {
        try {
            StringReader e = new StringReader(xml);
            return (T) this.createUnmarshaller().unmarshal(e);
        } catch (JAXBException var3) {
            throw new RuntimeException(var3);
        }
    }

    public <T> T fromXml(String xml, boolean caseSensitive) {
        try {
            String e = xml;
            if(!caseSensitive) {
                e = xml.toLowerCase();
            }

            StringReader reader = new StringReader(e);
            return (T) this.createUnmarshaller().unmarshal(reader);
        } catch (JAXBException var5) {
            throw new RuntimeException(var5);
        }
    }

    public Marshaller createMarshaller(String encoding) {
        try {
            Marshaller e = this.jaxbContext.createMarshaller();
            e.setProperty("jaxb.formatted.output", Boolean.TRUE);
            if(StringUtils.isNotBlank(encoding)) {
                e.setProperty("jaxb.encoding", encoding);
            }

            return e;
        } catch (JAXBException var3) {
            throw new RuntimeException(var3);
        }
    }

    public Unmarshaller createUnmarshaller() {
        try {
            return this.jaxbContext.createUnmarshaller();
        } catch (JAXBException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static class CollectionWrapper {
        @XmlAnyElement
        protected Collection collection;

        public CollectionWrapper() {
        }
    }
}
