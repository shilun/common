package com.common.annotation;


import com.common.util.model.EditorTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shilun on 17-1-6.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Editor {
    /*排序**/
    int seq() default 0;

    /*名称**/
    String name();

    /*显示**/
    boolean label() default false;

    /*值**/
    boolean value() default false;

    /*是否在列表中现示**/
    boolean list() default true;

    /*类型**/
    EditorTypeEnum type() default EditorTypeEnum.TextEditor;

    /*必选**/
    boolean request() default false;

    /*最大值**/
    int max() default 256;

    /*数据提供器**/
    String dataProvider() default "";


}
