package com.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;  
  
/** 
 * 自定义注解，标识主键字段需要自动增长 
 * <p> 
 * ClassName: AutoIncKey 
 * </p> 
 * <p> 
 * Copyright: (c)2017 Jastar·Wang,All rights reserved. 
 * </p> 
 *  
 * @author jastar-wang 
 * @date 2017年5月27日 
 */  
@Target(ElementType.FIELD)  
@Retention(RetentionPolicy.RUNTIME)  
public @interface AutoIncKey {  
      
}  