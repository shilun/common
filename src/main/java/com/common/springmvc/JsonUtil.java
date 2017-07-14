package com.common.springmvc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

public class JsonUtil {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final ObjectMapper mapper;

    public JsonUtil() {
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception var2) {
            throw new RuntimeException("转换json字符失败!");
        }
    }

    public static void main(String[] args) {

    }

    public <T> T toObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException var4) {
            throw new RuntimeException("将json字符转换为对象时失败!");
        }
    }

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper = new ObjectMapper();
        mapper.setDateFormat(dateFormat);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            public Object findSerializer(Annotated a) {
                if(a instanceof AnnotatedMethod) {
                    AnnotatedElement m = a.getAnnotated();
                    DateTimeFormat an = (DateTimeFormat)m.getAnnotation(DateTimeFormat.class);
                    if(an != null && !"yyyy-MM-dd HH:mm:ss".equals(an.pattern())) {
                        return new JsonUtil.JsonDateSerializer(an.pattern());
                    }
                }

                return super.findSerializer(a);
            }
        });
    }

    public static class JsonDateSerializer extends JsonSerializer<Date> {
        private SimpleDateFormat dateFormat;

        public JsonDateSerializer(String format) {
            this.dateFormat = new SimpleDateFormat(format);
        }

        public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
            String value = this.dateFormat.format(date);
            gen.writeString(value);
        }
    }
}
