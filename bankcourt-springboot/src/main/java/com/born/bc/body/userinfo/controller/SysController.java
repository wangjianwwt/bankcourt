package com.born.bc.body.userinfo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.born.bc.body.commons.entity.CommonCons;
import com.born.bc.body.commons.utils.CryptographyUtil;
import com.born.bc.body.commons.utils.LogUtils;
import com.born.bc.body.commons.utils.ResultJson;
import com.born.bc.body.userinfo.entity.User;
import com.born.bc.body.userinfo.service.api.UserServiceApi;

/**
 * 用户登录
 *
 * @author wangjian
 */
@Controller
public class SysController {

	@Autowired
	private UserServiceApi userApi;

	/**
	 * 用户登录
	 *
	 * @param user
	 * @param req
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/login")
	@ResponseBody
	public ResultJson login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
		try {

			if (StringUtils.isEmpty(user.getLoginName()) || StringUtils.isEmpty(user.getPassword())) {
				return ResultJson.buildParamError("用户名或者密码为空");
			}

			// 开始进行用户认证
			Subject currentUser = SecurityUtils.getSubject();
			// 密码加密
			String md5Pwd = CryptographyUtil.md5(user.getPassword(), user.getLoginName());

			UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), md5Pwd, user.isRememberMe());
			currentUser.login(token);

			// 使用MD5方式生成token
			String userToken = CryptographyUtil.md5(user.getPassword() + user.getLoginName(),
					new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

			// 登录日志
			Session session = currentUser.getSession();
			LogUtils.debug("sessionId:" + session.getId().toString());
			LogUtils.debug("sessionHost:" + session.getHost());
			LogUtils.debug("sessionTimeOut:" + session.getTimeout());

			// 获取用户基本信息
			User resultUser = userApi.getUserByLoginName(user.getLoginName());
			// 判断登录用户是否存在或者登录用户是否已经被禁用
			if (resultUser == null || CommonCons.EnableStatus_Disable.equals(resultUser.getEnableStatus())) {
				return ResultJson.buildError("用户：" + user.getLoginName() + "不存在!");
			}

			JSONObject userJson = convertUserToJsonObject(resultUser);

			// 得到用户信息JSON
			JSONObject userRolePermission = getUserRolePermission(user.getLoginName());

			JSONObject json = new JSONObject();
			// 设置用户登录基本
			json.put("userInfo", userJson);
			// 设置用户角色权限
			json.put("userRolePermission", userRolePermission);
			// 设置用户token
			json.put("userToken", userToken);
			// 设置sessionId
			json.put("jsessionid", session.getId().toString());

			JSONObject loginInfo = new JSONObject();
			loginInfo.put("lastLoginIp", resultUser.getLoginIp());
			loginInfo.put("lastLoginNum", resultUser.getLoginNum());
			loginInfo.put("lastLoginTime", resultUser.getLoginTime());

			// 设置登录信息
			json.put("loginInfo", loginInfo);

			Cookie cookie = new Cookie("JSESSIONID", session.getId().toString());
			response.addCookie(cookie);

			userApi.saveLoginInfo(request, resultUser.getId());

			LogUtils.debug("用户登录信息", json);
			LogUtils.debug("用户:" + user.getLoginName() + "登录成功");
			return ResultJson.buildSuccess("登录成功", json);
		} catch (Exception e) {
			LogUtils.error("用户:" + user.getLoginName() + "登录失败", e);
			return ResultJson.buildError("登录失败，用户名或密码错误!");
		}
	}

	/**
	 * 根据登录名将用户的角色和权限封装到一个统一的Json对象中
	 *
	 * @param loginName
	 * @return
	 */
	private JSONObject getUserRolePermission(String loginName) {

		// 获取用户权限角色信息
		HashSet<String> roles = userApi.getRoleByLoginName(loginName);
		// 判断是否存在角色信息
		if (roles.isEmpty()) {
			return null;
		}

		String roleStr = "";
		for (String item : roles) {
			roleStr += "," + item;
		}
		roleStr = (StringUtils.isEmpty(roleStr)) ? "" : roleStr.substring(1);

		// 根据角色ID列表获取角色对应的权限
		HashSet<String> permissions = userApi.getPermissionByRoleCodes(new ArrayList<String>(roles));

		String permissionStr = "";
		for (String item : permissions) {
			permissionStr += "," + item;
		}
		permissionStr = (StringUtils.isEmpty(permissionStr)) ? "" : permissionStr.substring(1);

		// 将用户角色权限信息封装
		JSONObject userJson = new JSONObject();
		userJson.put("roles", roleStr);
		userJson.put("permissions", permissionStr);
		return userJson;
	}

	/**
	 * 登出
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/layout")
	@ResponseBody
	public ResultJson layout(@RequestBody User user) {
		try {
			LogUtils.debug("退出登录操作.....");
			userApi.layout(user);
			return ResultJson.buildSuccess("登出成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("登出操作失败");
		}
	}

	/**
	 * 将用户信息转成json
	 *
	 * @param user
	 * @return
	 */
	private JSONObject convertUserToJsonObject(User user) {
		JSONObject json = new JSONObject();
		json.put("userId", user.getId());
		json.put("loginName", user.getLoginName());
		json.put("phone", user.getPhone());
		json.put("realName", user.getRealName());
		return json;
	}

}
