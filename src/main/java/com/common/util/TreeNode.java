//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.common.util;

import com.common.util.TreeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import java.util.List;

public class TreeNode extends TreeEntity<TreeNode> {
    private static final long serialVersionUID = 1L;
    private TreeNode parent;
    private String name;
    private String state;
    private String warning;
    private String group;
    private String nodeType;
    private String insRole;

    public TreeNode() {
    }

    public TreeNode(String id) {
        super(id);
    }

    @JsonBackReference
    @NotNull
    public TreeNode getParent() {
        return this.parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return this.parent != null && this.parent.getId() != null?this.parent.getId():"0";
    }

    @JsonIgnore
    public static void treeSortList(List<TreeNode> list, List<TreeNode> sourcelist, String parentId, Integer level) {
        try {
            Integer curLevel = level;
            Integer childCount = Integer.valueOf(0);

            for(int i = 0; i < sourcelist.size(); ++i) {
                TreeNode e = (TreeNode)sourcelist.get(i);
                if(e.getParent() != null && e.getParent().getId() != null && e.getParent().getId().equals(parentId)) {
                    e.setLevel(curLevel);
                    e.setParentId(e.getParent().getId());
                    e.setLeaf(true);
                    e.setExpanded(false);
                    e.setLoaded(true);
                    childCount = Integer.valueOf(0);

                    for(int j = 0; j < sourcelist.size(); ++j) {
                        TreeNode child = (TreeNode)sourcelist.get(j);
                        if(child.getParent() != null && child.getParent().getId() != null && child.getParent().getId().equals(e.getId())) {
                            childCount = Integer.valueOf(childCount.intValue() + 1);
                            e.setLeaf(false);
                            e.setExpanded(true);
                            list.add(e);
                            treeSortList(list, sourcelist, e.getId(), Integer.valueOf(level.intValue() + 1));
                            break;
                        }
                    }

                    if(childCount.intValue() == 0) {
                        list.add(e);
                    }
                }
            }
        } catch (Exception var12) {
            ;
        }

    }

    @JsonIgnore
    public static String getRootId() {
        return "1";
    }

    public String toString() {
        return this.name;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWarning() {
        return this.warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getNodeType() {
        return this.nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getInsRole() {
        return this.insRole;
    }

    public void setInsRole(String insRole) {
        this.insRole = insRole;
    }
}
