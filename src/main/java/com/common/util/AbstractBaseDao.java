//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import java.io.Serializable;
import java.util.List;

public interface AbstractBaseDao<T extends AbstractBaseEntity> extends Serializable {
    Long add(T var1);

    void up(T var1);

    T findById(Long var1);

    void delById(Long var1);

    List<T> query(T var1);

    T findByOne(T var1);

    int queryCount(T var1);
}
