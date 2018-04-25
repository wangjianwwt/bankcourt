package com.born.bc.body.commons.entity;

/**
 * 当前登录用户
 * @author wangjian
 */
public class CurrentLoginUser {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 真实姓名
     */
    private String realName;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 手机号码
     */
    private String phone;

    public CurrentLoginUser(){}

    public CurrentLoginUser(String userId, String loginName, String realName, String phone){
        this.userId = userId;
        this.loginName = loginName;
        this.realName = realName;
        this.phone = phone;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "CurrentLoginUser{" +
                "userId='" + userId + '\'' +
                ", loginName='" + loginName + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
