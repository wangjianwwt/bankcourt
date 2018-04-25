package com.born.bc.body.userinfo.entity;

public class ChangePwdVo {

	/**原密码*/
	private String oldPassword;
	/**新密码*/
	private String newPassword;
	/**确认新密码*/
	private String reNewPassword;
	/**登录名*/
	private String loginName;
	/**token*/
	private String userToken;
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getReNewPassword() {
		return reNewPassword;
	}
	public void setReNewPassword(String reNewPassword) {
		this.reNewPassword = reNewPassword;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	@Override
	public String toString() {
		return "{\"oldPassword\":\"" + oldPassword + "\", \"newPassword\":\"" + newPassword + "\", \"reNewPassword\":\""
				+ reNewPassword + "\", \"loginName\":\"" + loginName + "\", \"userToken\":\"" + userToken + "\"}";
	}
	
}
