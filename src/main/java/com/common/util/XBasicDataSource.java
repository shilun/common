//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.exception.ApplicationException;
import com.jolbox.bonecp.BoneCPDataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

public class XBasicDataSource extends BoneCPDataSource {
    private static final long serialVersionUID = 1L;

    public XBasicDataSource() {
    }

    public synchronized void close() {
        try {
            DriverManager.deregisterDriver(DriverManager.getDriver(this.getJdbcUrl()));
        } catch (SQLException var2) {
            throw new ApplicationException("关闭数据连接失败");
        }

        super.close();
    }
}
