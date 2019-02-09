package com.common.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;  
  
/** 
 * 模拟序列类 
 *  
 * @author Jastar·Wang 
 * @date 2017年5月27日 
 */  
@Document(collection = "sequence")  
public class SeqInfo {  
  
    @Id  
    private String id;// 主键  
  
    @Field  
    private String collName;// 集合名称  
  
    @Field  
    private Long seqId;// 序列值  

}  