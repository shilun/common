package com.common.util.model;

import java.util.Date;

/**
 * Created by shilun on 17-1-6.
 */
public enum EditorTypeEnum {
    /*文本**/
    TextEditor(String.class),
    /*大文本**/
    TextAreaEditor(Double.class),
    /*数字**/
    NumberEditor(Long.class),
    /*小数**/
    DoubleEditor(Double.class),
    /*下拉列表**/
    DropDownEditor(String.class),
    /*日期**/
    DateEditor(Date.class),;

    EditorTypeEnum(Class<?> editor) {
        this.editor = editor;
    }

    private Class<?> editor;
}
