package com.born.bc.sysconfig.realm;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.born.bc.body.commons.utils.CryptographyUtil;
import com.born.bc.body.commons.utils.LogUtils;
import com.born.bc.body.userinfo.entity.User;
import com.born.bc.body.userinfo.service.api.UserServiceApi;

/**
 * 自定义realm
 * 
 * @author wangjian
 *
 */
public class MyShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserServiceApi userApi;

	/**
	 * 为当前登录成功的用户授予角色和权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		try {
			// 拿到登录名
			String loginName = String.valueOf(principals.getPrimaryPrincipal());
			// 校验loginName是否为空
			if (StringUtils.isEmpty(loginName)) {
				return null;
			}
			// 获得loginName关联的角色列表
			HashSet<String> roles = userApi.getRoleByLoginName(loginName);
			LogUtils.debug("角色数据：", roles.toArray());
			if (roles.isEmpty()) {
				return null;
			}
			// 根据角色ID列表获取角色对应的权限
			HashSet<String> permissions = userApi.getPermissionByRoleCodes(new ArrayList<String>(roles));
			LogUtils.debug("权限数据：", permissions.toArray());
			if (permissions.isEmpty()) {
				return null;
			}

			SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
			// 设置角色的Code作为角色取证
			authorizationInfo.setRoles(roles);
			authorizationInfo.setStringPermissions(permissions);
			return authorizationInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String loginName = String.valueOf(token.getPrincipal());
		LogUtils.debug("登录名：", loginName);
		User resultUser = userApi.getUserByLoginName(loginName);
		LogUtils.debug("登录用户：", resultUser);

		if (resultUser != null) {
			String realmName = resultUser.getLoginName() + "," + resultUser.getPassword();
			LogUtils.debug("realmName：", realmName);
			
			// 密码加密
			String md5Pwd = CryptographyUtil.md5(resultUser.getPassword(), null);
			
			AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(resultUser.getLoginName(),
					md5Pwd, realmName);
			LogUtils.debug("authcInfo：", authcInfo.getCredentials()+"==="+authcInfo.getPrincipals());
			return authcInfo;
		}
		
		return null;
	}

}
