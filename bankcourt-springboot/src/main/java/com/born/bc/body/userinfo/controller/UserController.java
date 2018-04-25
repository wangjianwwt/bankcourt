package com.born.bc.body.userinfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.born.bc.body.commons.entity.CommonCons;
import com.born.bc.body.commons.entity.PageParamVO;
import com.born.bc.body.commons.utils.ResultJson;
import com.born.bc.body.userinfo.entity.ChangePwdVo;
import com.born.bc.body.userinfo.entity.User;
import com.born.bc.body.userinfo.service.api.UserServiceApi;

/**
 * 用户管理Controller
 * 
 * @author wangjian
 */
@RequestMapping(value = "/user")
@Controller
public class UserController {

	@Autowired
	private UserServiceApi userApi;

	/**
	 * 分页查询用户列表
	 * 
	 * @param user
	 * @param page
	 * @return
	 */
	@GetMapping(value = "/list")
	@ResponseBody
	public ResultJson list(User user, PageParamVO page) {
		try {
			return userApi.findUserByPage(user, page);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("系统出现异常，请及时联系系统开发工程师进行系统修复！");
		}
	}

	/**
	 * 启用
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/enable")
	@ResponseBody
	public ResultJson enable(@RequestBody User user) {

		try {
			return userApi.updateUserEnableStatus(user.getId(), CommonCons.EnableStatus_Enable);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("系统出现异常，请及时联系系统开发工程师进行系统修复！");
		}
	}

	/**
	 * 停用
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/disable")
	@ResponseBody
	public ResultJson disable(@RequestBody User user) {

		try {
			return userApi.updateUserEnableStatus(user.getId(), CommonCons.EnableStatus_Disable);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("系统出现异常，请及时联系系统开发工程师进行系统修复！");
		}

	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/update")
	@ResponseBody
	public ResultJson update(@RequestBody User user) {
		try {
			return userApi.updateUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("系统出现异常，请及时联系系统开发工程师进行系统修复！");
		}
	}

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/add")
	@ResponseBody
	public ResultJson add(@RequestBody User user) {
		try {
			return userApi.insertUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("系统出现异常，请及时联系系统开发工程师进行系统修复！");
		}
	}

	/**
	 * 重置密码
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/changepwd")
	@ResponseBody
	public ResultJson changePassowrd(@RequestBody ChangePwdVo vo) {
		try {
			return userApi.changePassword(vo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("系统出现异常，请及时联系系统开发工程师进行系统修复！");
		}
	}

	/**
	 * 删除
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/del")
	@ResponseBody
	public ResultJson del(@RequestBody User user) {
		try {
			if (StringUtils.isEmpty(user.getId())) {
				return ResultJson.buildParamError("参数异常");
			}
			userApi.updateUserDelStatus(user.getId());
			return ResultJson.buildSuccess("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("系统出现异常，请及时联系系统开发工程师进行系统修复！");
		}
	}

}
