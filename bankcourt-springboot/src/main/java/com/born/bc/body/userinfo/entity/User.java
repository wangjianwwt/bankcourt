package com.born.bc.body.userinfo.entity;

import java.util.Date;
import java.util.List;

import com.born.bc.body.commons.entity.BaseEntity;
import com.born.bc.body.userinfo.entity.cons.EnableStatusEnum;

/**
 * 用户类
 * @author wangjian
 */
public class User extends BaseEntity{

    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 手机号(多个使用","隔开)
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 启用状态
     * 1启用（默认）
     * 0未启用
     */
    private Integer enableStatus;
    /**
     * 启用中文字符
     */
    private String enableStatusStr;
    /**
     * 是否管理员
     * 1是，0否
     */
    private Integer isAdmin;
    /**登录IP*/
    private String loginIp;
    /**登录时间*/
    private Date loginTime;
    /**登录次数*/
    private Integer loginNum;
    
    /**
     * 关联角色列表
     */
    private List<Role> roles;
    /**
     * 角色拼接字符串，展示用
     */
    private String rolesStr;
    
    private boolean rememberMe;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

	public String getEnableStatusStr() {
		return enableStatusStr;
	}

	public void setEnableStatusStr(String enableStatusStr) {
		this.enableStatusStr = enableStatusStr;
	}

	public String getRolesStr() {
		return rolesStr;
	}

	public void setRolesStr(String rolesStr) {
		this.rolesStr = rolesStr;
	}

	public Integer getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Integer getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(Integer loginNum) {
		this.loginNum = loginNum;
	}

	@Override
	public String toString() {
		return "{\"loginName\":\"" + loginName + "\", \"password\":\"" + password + "\", \"realName\":\"" + realName
				+ "\", \"phone\":\"" + phone + "\", \"email\":\"" + email + "\", \"enableStatus\":\"" + enableStatus
				+ "\", \"enableStatusStr\":\"" + enableStatusStr + "\", \"isAdmin\":\"" + isAdmin + "\", \"loginIp\":\""
				+ loginIp + "\", \"loginTime\":\"" + loginTime + "\", \"loginNum\":\"" + loginNum + "\", \"roles\":\""
				+ roles + "\", \"rolesStr\":\"" + rolesStr + "\"}";
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
    
    
}