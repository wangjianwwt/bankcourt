package com.born.bc.body.userinfo.entity;

public class PermissionLinkDto {

	//权限ID
	private String permissionId;
	//权限名称
	private String permissionName;
	//是否关联
	private Integer isLinked;
	public String getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public Integer getIsLinked() {
		return isLinked;
	}
	public void setIsLinked(Integer isLinked) {
		this.isLinked = isLinked;
	}
	
	
	
}
