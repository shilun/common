//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.worker;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class WorkerSchedulerBean extends SchedulerFactoryBean {
    private String id = UUID.randomUUID().toString().replaceAll("-", "");
    private String remark;
    private Integer index;
    private String group;
    private Integer groupIndex;
    private String name;

    @JsonProperty
    public String getName() {
        return this.name;
    }

    @JsonProperty
    public Boolean getStatus() {
        return Boolean.valueOf(this.isRunning());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public WorkerSchedulerBean() {
    }

    @JsonProperty
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIndex() {
        return this.index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getGroupIndex() {
        return this.groupIndex;
    }

    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }
}
