//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.util.AbstractBaseEntity;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AbstractBaseService<T extends AbstractBaseEntity> extends Serializable {
    Long add(T var1);

    void up(T var1);

    void save(T var1);

    T findById(Long var1);

    void delById(Long var1);

    List<T> query(T var1);

    int queryCount(T var1);

    Page<T> queryByPage(T var1, Pageable var2);

    T findByOne(T var1);
}
