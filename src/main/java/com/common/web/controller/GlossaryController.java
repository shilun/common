package com.common.web.controller;


import com.common.codetable.CodeTableFactory;
import com.common.codetable.CodeTableUtils;
import com.common.exception.BizException;
import com.common.util.AbstractExtendCodeTable;
import com.common.util.MateDataInfo;
import com.common.util.PropertyUtil;
import com.common.util.ServiceUtils;
import com.common.util.model.YesOrNoEnum;
import com.common.web.AbstractController;
import com.common.web.IExecute;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lunsh on 2016/7/22.
 */
@Controller
@RequestMapping(value = "/glossery")
public class GlossaryController extends AbstractController {


    private ServiceUtils serviceUtils;

    @RequestMapping(value = "/buildGlossery")
    @ResponseBody
    public Map<String, Object> buildGlossery(final String codeType) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                List<Map<String, Object>> keyValues = new ArrayList<Map<String, Object>>();
                AbstractExtendCodeTable codeTable = null;
                try {
                    MateDataInfo mateData = CodeTableFactory.findByCode(codeType);
                    codeTable = (AbstractExtendCodeTable) mateData.getType().newInstance();

                    List<AbstractExtendCodeTable> query = serviceUtils.getService(codeTable.getClass()).query(codeTable);
                    Map<String, String> mate = CodeTableUtils.getCodeTableMate((Class<? extends AbstractExtendCodeTable>) mateData.getType());
                    for (AbstractExtendCodeTable code : query) {
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("value", PropertyUtil.getProperty(code, mate.get("value")));
                        item.put("name", PropertyUtil.getProperty(code, mate.get("label")));
                        keyValues.add(item);
                    }
                } catch (Exception e) {
                    codeTable.setDelStatus(YesOrNoEnum.NO.getValue());
                }
                return keyValues;
            }
        });
    }

    @RequestMapping(value = "/buildProviderData")
    @ResponseBody
    public Map<String, Object> buildProviderData(final String codeType) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                List<Map<String, Object>> datalist = new ArrayList<>();
                MateDataInfo mateDataInfo = CodeTableFactory.findByCode(codeType);
                Map<String, String> codeTableDataProviders = CodeTableUtils.getCodeTableDataProviders((Class<? extends AbstractExtendCodeTable>) mateDataInfo.getType());
                for (String code : codeTableDataProviders.keySet()) {
                    String itemType = codeTableDataProviders.get(code);
                    try {
                        MateDataInfo mateItem = CodeTableFactory.findByCode(itemType);
                        AbstractExtendCodeTable table = (AbstractExtendCodeTable) mateItem.getType().newInstance();
                        Map<String, String> mate = CodeTableUtils.getCodeTableMate(table.getClass());
                        table.setDelStatus(YesOrNoEnum.NO.getValue());
                        List query = serviceUtils.getService(table.getClass()).query(table);
                        List<Map<String, Object>> itemList = new ArrayList<>();
                        for (Object o : query) {
                            Map<String, Object> item = new HashMap<>();
                            item.put("value", PropertyUtil.getProperty(o, mate.get("value")));
                            item.put("name", PropertyUtil.getProperty(o, mate.get("label")));
                            itemList.add(item);
                        }
                        Map<String, Object> pushItem = new HashMap<>();
                        pushItem.put("name", code);
                        pushItem.put("data", itemList);
                        datalist.add(pushItem);
                    } catch (Exception e) {
                        throw new BizException("bind.sampleCode.error", "绑定下拉列表失败");
                    }
                }
                return datalist;
            }
        });
    }
}
