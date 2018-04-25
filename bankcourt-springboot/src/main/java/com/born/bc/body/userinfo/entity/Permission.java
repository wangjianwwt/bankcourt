package com.born.bc.body.userinfo.entity;

import com.born.bc.body.commons.entity.BaseEntity;
import com.born.bc.body.userinfo.entity.cons.EnableStatusEnum;

/**
 * 权限类
 * @author wangjian
 */
public class Permission extends BaseEntity{
    /**
     * 权限名
     */
    private String permissionName;
    /**
     * 权限编码
     */
    private String permissionCode;
    /**
     * 启用状态
     * 1启用
     * 0未启用(默认)
     */
    private Integer enableStatus;
    /**
     * 启用状态中文值
     */
    private String enableStatusStr;
    /**
     * 备注
     */
    private String comments;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
        //判断设置枚举值
        if(enableStatus != null){
        	setEnableStatusStr(EnableStatusEnum.getLabel(enableStatus));
        }
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

	public String getEnableStatusStr() {
		return enableStatusStr;
	}

	public void setEnableStatusStr(String enableStatusStr) {
		this.enableStatusStr = enableStatusStr;
	}
}