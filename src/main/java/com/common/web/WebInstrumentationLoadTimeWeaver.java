//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.web;

import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;

import java.lang.instrument.ClassFileTransformer;

public class WebInstrumentationLoadTimeWeaver extends InstrumentationLoadTimeWeaver {
    public WebInstrumentationLoadTimeWeaver() {
    }

    public void addTransformer(ClassFileTransformer transformer) {
        super.addTransformer(transformer);
    }
}
