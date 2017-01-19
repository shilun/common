//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web;

import com.common.util.Money;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.apache.commons.lang.StringUtils;

public class CustomMoneyEditor extends PropertyEditorSupport {
    public CustomMoneyEditor() {
    }

    public void setAsText(String text) throws IllegalArgumentException {
        if(StringUtils.isBlank(text)) {
            this.setValue((Object)null);
        } else {
            this.setValue(new Money(new BigDecimal(text)));
        }
    }

    public String getAsText() {
        Money money = (Money)this.getValue();
        if(money != null) {
            new DecimalFormat(",###.##");
            return String.valueOf(money.getAmount());
        } else {
            return super.getAsText();
        }
    }
}
