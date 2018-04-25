package com.born.bc.body.commons.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类公共属性
 * @author wangjian
 */
public class BaseEntity implements Serializable{

    private static final long serialVersionUID = 5758482603256924747L;

    /**
     * userToken
     */
    private String userToken;

    /**
     * 主键ID
     */
    private String id;
    /**
     * 创建人ID
     */
    private String formmakerId;
    /**
     * 创建人名
     */
    private String formmakerName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后修改时间
     */
    private Date lastModifiedTime;
    /**
     * 删除状态
     * 1已删除
     * 0位删除（默认）
     */
    private Integer delStatus;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken == null ? null : userToken.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFormmakerId() {
        return formmakerId;
    }

    public void setFormmakerId(String formmakerId) {
        this.formmakerId = formmakerId == null ? null : formmakerId.trim();
    }

    public String getFormmakerName() {
        return formmakerName;
    }

    public void setFormmakerName(String formmakerName) {
        this.formmakerName = formmakerName == null ? null : formmakerName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }
}
