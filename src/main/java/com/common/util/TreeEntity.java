//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.fasterxml.jackson.annotation.JsonBackReference;

public abstract class TreeEntity<T> {
    private static final long serialVersionUID = 1L;
    private String id;
    protected T parent;
    protected String parentIds;
    protected String name;
    protected Integer sort;
    protected Integer level;
    protected String parentId;
    protected boolean leaf;
    protected boolean expanded;
    protected boolean loaded;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public boolean isLeaf() {
        return this.leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public TreeEntity() {
        this.sort = Integer.valueOf(30);
    }

    public TreeEntity(String id) {
        this.id = id;
    }

    @JsonBackReference
    public abstract T getParent();

    public abstract void setParent(T var1);

    public String getParentIds() {
        return this.parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return this.parentId;
    }
}
