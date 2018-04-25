package com.born.bc.body.userinfo.entity;

import java.util.Arrays;

public class RoleParamVO {
	/**主键ID*/
	private String id;
	/**权限*/
	private String [] permissions;
	/**用户token*/
	private String userToken;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"permissions\":\"" + Arrays.toString(permissions) + "\", \"userToken\":\""
				+ userToken + "\"}";
	}
	
	
}
