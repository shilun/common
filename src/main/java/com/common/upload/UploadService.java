//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.upload;

import com.common.exception.BizException;
import com.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class UploadService {
    private static final Logger logger = Logger.getLogger(UploadService.class);
    private MongoDBUtil mongoDBUtil;
    private static final String upload_file = "upload_file";

    public UploadService() {
    }

    public String uploadFile(byte[] bytes) {
        if (bytes == null) {
            throw new BizException("upload.error.no.data", "upload no data");
        } else {
            String uuid = StringUtils.getUUID();
            HashMap entity = new HashMap();
            entity.put("id", uuid);
            entity.put("fileData", bytes);
            this.mongoDBUtil.insert("upload_file", entity, Boolean.valueOf(true));
            return uuid;
        }
    }

    public byte[] findFileById(String id) {
        HashMap entity = new HashMap();
        entity.put("id", id);
        Map one = this.mongoDBUtil.findOne("upload_file", entity);
        if (one == null) {
            throw new BizException("find.file.error", "find.no.data");
        } else {
            return (byte[]) ((byte[]) one.get("fileData"));
        }
    }

    public void setMongoDBUtil(MongoDBUtil mongoDBUtil) {
        this.mongoDBUtil = mongoDBUtil;
    }
}
