package com.common.mongo;

import com.common.util.AbstractBaseEntity;
import com.common.util.ISplitProxy;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;

public class SaveMongoEventListener extends AbstractMongoEventListener<AbstractBaseEntity> {
    @Resource
    private MongoTemplate mongoTemplate;

    public void onBeforeConvert(BeforeConvertEvent event) {
        if (event != null) {
            AbstractBaseEntity entity = (AbstractBaseEntity) event.getSource();
            if(entity instanceof ISplitProxy){
                ISplitProxy proxyEntity= (ISplitProxy) entity;
                entity.setId(getNextId(entity.getClass().getSimpleName()+"_"+proxyEntity.getProxyId()));
            }
            else{
                entity.setId(getNextId(entity.getClass().getSimpleName()));
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
    private Long getNextId(String collName, Long proxyId) {
        Query query = new Query(Criteria.where("collName").is(collName).and("proxyId").is(proxyId));
        Update update = new Update();
        update.inc("seqId", new Long(1));
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
        if (seqId.getSeqId() < 300000) {
            return getNextId(collName, proxyId, 300000 - seqId.getSeqId());
        }
        return seqId.getSeqId();
    }
    /**
     * 获取下一个自增ID
     *
     * @param collName 集合名
     * @return
     * @author yinjihuan
     */
    private Long getNextId(String collName, Long proxyId, long maxid) {
        Query query = new Query(Criteria.where("collName").is(collName).and("proxyId").is(proxyId));
        Update update = new Update();
        update.inc("seqId", maxid);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
        return seqId.getSeqId();
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
        update.inc("seqId", new Long(1));
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
        return seqId.getSeqId();
    }

}