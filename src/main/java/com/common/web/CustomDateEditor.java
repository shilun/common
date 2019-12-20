//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomDateEditor extends PropertyEditorSupport {
    private final boolean allowEmpty;
    private DateFormat dateFormat;

    public CustomDateEditor(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    public void setAsText(String text) throws IllegalArgumentException {
        if(this.allowEmpty && !StringUtils.hasText(text)) {
            this.setValue((Object)null);
        } else {
            try {
                if(text.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                    this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else if(text.matches("\\d{4}年\\d{2}月\\d{2}日")) {
                    this.dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                } else if(text.matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
                    this.dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                } else {
                    if(text.matches("[A-Za-z]{3} [A-Za-z]{3} \\d{2} \\d{2}:\\d{2}:\\d{2} CST \\d{4}")) {
                        this.dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss \'CST\' yyyy", Locale.US);
                        this.setValue(this.dateFormat.parse(text));
                        return;
                    }
                    this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                }
                this.dateFormat.setLenient(false);
                this.setValue(this.dateFormat.parse(text));
            } catch (ParseException var3) {
                throw new IllegalArgumentException("Could not parse date: " + var3.getMessage(), var3);
            }
        }
    }

    public String getAsText() {
        Date value = (Date)this.getValue();
        if(this.dateFormat != null) {
            this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        return value != null?this.dateFormat.format(value):"";
    }
}
