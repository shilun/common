//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.util.Money;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class MoneyTypeHandler implements TypeHandler<Money> {
    public MoneyTypeHandler() {
    }

    public void setParameter(PreparedStatement ps, int i, Money parameter, JdbcType jdbcType) throws SQLException {
        if(parameter == null) {
            ps.setLong(i, 0L);
        } else {
            ps.setLong(i, parameter.getCent());
        }

    }

    public Money getResult(ResultSet rs, String columnName) throws SQLException {
        return new Money(rs.getLong(columnName));
    }

    public Money getResult(ResultSet rs, int columnIndex) throws SQLException {
        return new Money(rs.getLong(columnIndex));
    }

    public Money getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return new Money(cs.getLong(columnIndex));
    }
}
