package com.common.mongo;

import com.common.annotation.GeneratedValue;
import com.common.util.AbstractBaseEntity;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;

public class SaveMongoEventListener extends AbstractMongoEventListener<AbstractBaseEntity> {
    @Resource
    private MongoTemplate mongoTemplate;

    public void onBeforeConvert(BeforeConvertEvent event) {
        if (event != null) {
            AbstractBaseEntity entity = (AbstractBaseEntity) event.getSource();
            if (entity.getId() == null) {
                entity.setId(getNextId(entity.getClass().getSimpleName()));
                return;
            }
        }
    }

    /**
     * 获取下一个自增ID
     *
     * @param collName 集合名
     * @return
     * @author yinjihuan
     */
    private Long getNextId(String collName) {
        Query query = new Query(Criteria.where("collName").is(collName));
        Update update = new Update();
        update.inc("seqId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
        return seqId.getSeqId();
    }

}