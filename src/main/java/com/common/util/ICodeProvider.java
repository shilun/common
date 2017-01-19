package com.common.util;

import com.common.util.IGlossary;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public interface ICodeProvider {
    IGlossary getItem(Integer var1);

    IGlossary getItem(Long var1);

    IGlossary getItemByCode(String var1);

    List<IGlossary> getItemsByParentCode(String var1);

    String getCode(Integer var1);

    String getTableSelectOptions(JdbcTemplate var1, String var2, String var3, String var4, String var5);
}
