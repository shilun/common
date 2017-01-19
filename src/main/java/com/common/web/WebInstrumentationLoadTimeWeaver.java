//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web;

import java.lang.instrument.ClassFileTransformer;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;

public class WebInstrumentationLoadTimeWeaver extends InstrumentationLoadTimeWeaver {
    public WebInstrumentationLoadTimeWeaver() {
    }

    public void addTransformer(ClassFileTransformer transformer) {
        super.addTransformer(transformer);
    }
}
