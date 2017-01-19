package com.common.codetable;

import com.common.annotation.Editor;
import com.common.util.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shilun on 17-1-10.
 */
public class CodeTableUtils {

    public static List<EditorDto> buildProperyties(Class<? extends AbstractExtendCodeTable> type) {
        PropertyDescriptor[] descriptors = PropertyUtil.getPropertyDescriptors(type);
        List<EditorDto> list = new ArrayList<>();
        for (PropertyDescriptor descriptor : descriptors) {
            Method readMethod = descriptor.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            Editor annotation = readMethod.getAnnotation(Editor.class);
            if (annotation != null) {
                EditorDto dto = new EditorDto();
                dto.setSeq(annotation.seq());
                dto.setValue(annotation.value());
                dto.setList(annotation.list());
                dto.setLabel(annotation.label());
                dto.setMax(annotation.max());
                dto.setName(annotation.name());
                dto.setPropertyName(descriptor.getName());
                dto.setRequest(annotation.request());
                dto.setDataProvider(annotation.dataProvider());
                dto.setType(annotation.type());
                list.add(dto);
            }
        }
        return list;
    }


    public static Map<String, String> getCodeTableMate(Class<? extends AbstractExtendCodeTable> type) {
        Map<String, String> map = new HashMap<>();
        List<EditorDto> editors = buildProperyties(type);
        for (EditorDto editor : editors) {
            if (editor.getLabel()) {
                map.put("label", editor.getPropertyName());
            }
            if (editor.isValue()) {
                map.put("value", editor.getPropertyName());
            }
        }
        return map;
    }


    public static Map<String, String> getCodeTableDataProviders(Class<? extends AbstractExtendCodeTable> type) {
        Map<String, String> map = new HashMap<>();
        List<EditorDto> editors = buildProperyties(type);
        for (EditorDto editor : editors) {
            if (StringUtils.isNotBlank(editor.getDataProvider())) {
                map.put(editor.getPropertyName(), editor.getDataProvider());
            }
        }

        return map;
    }

}