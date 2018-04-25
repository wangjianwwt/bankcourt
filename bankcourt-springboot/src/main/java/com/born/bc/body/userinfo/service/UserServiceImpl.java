package com.born.bc.body.userinfo.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.util.StringUtils;
import com.born.bc.body.commons.entity.CommonCons;
import com.born.bc.body.commons.entity.CurrentLoginUser;
import com.born.bc.body.commons.entity.PageParamVO;
import com.born.bc.body.commons.utils.CryptographyUtil;
import com.born.bc.body.commons.utils.IdGenerator;
import com.born.bc.body.commons.utils.LogUtils;
import com.born.bc.body.commons.utils.ResultJson;
import com.born.bc.body.userinfo.dao.PermissionMapper;
import com.born.bc.body.userinfo.dao.RoleMapper;
import com.born.bc.body.userinfo.dao.UserMapper;
import com.born.bc.body.userinfo.entity.ChangePwdVo;
import com.born.bc.body.userinfo.entity.Role;
import com.born.bc.body.userinfo.entity.User;
import com.born.bc.body.userinfo.entity.UserRoleRelation;
import com.born.bc.body.userinfo.service.api.UserServiceApi;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 用户业务逻辑实现类
 * 
 * @author wangjian
 */
@Service
public class UserServiceImpl implements UserServiceApi {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PermissionMapper permissionMapper;


	
	public User getUserByLoginName(String loginName) {
		// 根据用户名或者电话号码查询启用用户信息
		return userMapper.getUserByLoginName(loginName);
	}

	
	public int updateUserDelStatus(String id) {
		// 逻辑删除用户
		userMapper.updateUserDelStatus(id);
		// 逻辑删除用户与角色的关联关系
		userMapper.updateUserAndRoleDelStatus(id);
		return 0;
	}

	
	public HashSet<String> getRoleByLoginName(String loginName) {
		return roleMapper.getRoleByLoginName(loginName);
	}

	
	public HashSet<String> getPermissionByRoleCodes(List<String> roleCodes) {
		return permissionMapper.getPermissionByRoleCodes(roleCodes);
	}

	
	public ResultJson findUserByPage(User user, PageParamVO page) {
		PageHelper.startPage(page.getPage(), page.getLimit());
		//查询用户
		List<User> list = userMapper.findUserList(user);
		if(CollectionUtils.isEmpty(list)){
			return ResultJson.buildSuccess("查询成功");
		}
		// 查询用户下对应的角色
		ArrayList<Map<String, String>> roleList = userMapper.findRoleListByUserIds(list);
		// 校验查询角色数据
		if(!CollectionUtils.isEmpty(roleList)){
			for(User us : list){
				String rolesStr = "";
				String userId = us.getId();
				List<Role> roles = new ArrayList<Role>(3);
				for(Map<String, String> map : roleList){
					if(userId.equals(map.get("userId"))){
						Role role = new Role();
						role.setId(map.get("id"));
						role.setRoleCode(map.get("roleCode"));
						role.setRoleName(map.get("roleName"));
						roles.add(role);
						
						// 拼接角色字符串
						rolesStr += ","+role.getRoleName();
						break;
					}
				}
				
				rolesStr = (StringUtils.isEmpty(rolesStr)) ? rolesStr : rolesStr.substring(1);
				us.setRolesStr(rolesStr);
				us.setRoles(roles);
				
				// 将用户的密码设置为空 ,对外不可见
				us.setPassword(null);
			}
		}
		
		PageInfo<User> pageInfo = new PageInfo<User>(list);
		return ResultJson.buildSuccess("查询成功", pageInfo);
	}

	
	@Transactional(rollbackFor = Exception.class)
	public ResultJson updateUserEnableStatus(String id, Integer enableStatus) {
		if (StringUtils.isEmpty(id)) {
			return ResultJson.buildParamError("请求口令不匹配，请稍后再试！");
		}
		// 根据用户id查询
		User user = new User();
		user.setId(id);
		user.setEnableStatus(enableStatus);
		userMapper.updateByPrimaryKeySelective(user);
		return ResultJson.buildSuccess("用户状态修改成功!");
	}

	
	@Transactional(rollbackFor = Exception.class)
	public ResultJson updateUser(User user) {
		// 参数校验
		if (StringUtils.isEmpty(user.getId())) {
			return ResultJson.buildParamError("请求口令不匹配，请稍后再试！");
		}

		ResultJson resultJson = validateUser(user);
        if (resultJson != null) {
            return resultJson;
        }

		// 校验用户名和手机号码
		ArrayList<User> users = userMapper.validateUserByLoginName(user.getId(), user.getLoginName(), user.getPhone());
		if (!users.isEmpty()) {
			return ResultJson.buildError("登录名或者手机号码已被占用!");
		}

		// 修改操作，不能修改登录名
		user.setLoginName(null);
		userMapper.updateByPrimaryKeySelective(user);

		// 删除旧的用户角色关系
		userMapper.updateUserAndRoleDelStatus(user.getId());

		// 新建用户角色关系
		createUserAndRoleRelation(user);

		return ResultJson.buildSuccess("修改用户成功");
	}

	
	@Transactional(rollbackFor = Exception.class)
	public ResultJson insertUser(User user) {
		// 参数校验
		ResultJson resultJson = validateUser(user);
        if (resultJson != null) {
            return resultJson;
        }
		// 校验登录名和手机号码的唯一
		ArrayList<User> users = userMapper.validateUserByLoginName(null, user.getLoginName(), user.getPhone());
		if (!users.isEmpty()) {
			return ResultJson.buildError("登录名或者手机号码已被占用!");
		}

		CurrentLoginUser loginUser = new CurrentLoginUser();
		user.setFormmakerId(loginUser.getUserId());
		user.setFormmakerName(loginUser.getLoginName());
		user.setId(IdGenerator.getUUID());
		
		// 设置用户的默认密码
		String md5Pwd = CryptographyUtil.md5(CommonCons.Default_Password, user.getLoginName());
		user.setPassword(md5Pwd);

		// 创建用户
		userMapper.insertSelective(user);
		// 新建用户角色关系
		createUserAndRoleRelation(user);

		return ResultJson.buildSuccess("新增用户成功");
	}

	
	public ResultJson queryUserInfo(String userId) {
		try {
			if (StringUtils.isEmpty(userId)) {
				return ResultJson.buildError("参数异常!");
			}

			// 用户信息
			User user = userMapper.selectById(userId);
			// 用户角色关系
			List<Role> linkedRols = roleMapper.selectLinkedRoleByUserId(userId);
			// 获取所有角色做对比
			List<Role> freeRoles = roleMapper.selectRoleList(new Role());

			Map<String, Object> userInfoMap = new HashMap<String, Object>();
			userInfoMap.put("user", user);
			userInfoMap.put("linkedRols", linkedRols);
			userInfoMap.put("freeRoles", freeRoles);

			return ResultJson.buildSuccess("查询成功!", userInfoMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("查询失败!");
		}
	}

	/**
	 * 创建用户角色关系
	 * 
	 * @param user
	 */
	private void createUserAndRoleRelation(User user) {
		// 得到参数中的角色
		List<Role> roles = user.getRoles();

		Set<String> relationSet = new HashSet<String>();

		for (Role role : roles) {
			relationSet.add(role.getId());
		}

		CurrentLoginUser loginUser = new CurrentLoginUser();

		List<UserRoleRelation> relations = new ArrayList<UserRoleRelation>();

		for (String roleId : relationSet) {
			UserRoleRelation relation = new UserRoleRelation();
			relation.setId(IdGenerator.getUUID());
			relation.setUserId(user.getId());
			relation.setRoleId(roleId);
			relation.setFormmakerId(loginUser.getUserId());
			relation.setFormmakerName(loginUser.getLoginName());
			relations.add(relation);
		}

		// 新建用户角色关联关系
		userMapper.batchInsertUserRoleRelation(relations);
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public ResultJson changePassword(ChangePwdVo vo) throws Exception {

		LogUtils.debug("修改用户密码", vo);
		// 校验参数
		if(StringUtils.isEmpty(vo.getLoginName()) 
				|| StringUtils.isEmpty(vo.getNewPassword()) 
				|| StringUtils.isEmpty(vo.getOldPassword()) 
				||StringUtils.isEmpty(vo.getReNewPassword()) 
				|| StringUtils.isEmpty(vo.getUserToken())){
			return ResultJson.buildError("参数异常");
		}
		
		if(vo.getOldPassword().equals(vo.getNewPassword())){
			return ResultJson.buildError("原密码不能与新密码一致");
		}
		if(!vo.getNewPassword().equals(vo.getReNewPassword())){
			return ResultJson.buildError("两次新密码输入不一致，请确认后再输入");
		}
		
		// 获取用户基本信息
        User resultUser = userMapper.getUserByLoginName(vo.getLoginName());
        if(resultUser == null){
        	return ResultJson.buildError("用户"+vo.getLoginName()+"不存在或者用户已经被禁用，无法重新设置密码!");
        }
        // 验证旧密码
        String oldMd5Pwd = CryptographyUtil.md5(vo.getOldPassword(), vo.getLoginName());
        Integer count = userMapper.validateOldMd5Pwd(resultUser.getId(), oldMd5Pwd);
        if(count == null || count < 1){
        	return ResultJson.buildError("原密码错误！");
        }
        
        // 申明一个空用户用来更新用户密码
        User user = new User();
		// 对用户密码进行MD5加密
		String newMd5Pwd = CryptographyUtil.md5(vo.getNewPassword(), vo.getLoginName());
		// 重置密码
		user.setPassword(newMd5Pwd);
		user.setLoginName(vo.getLoginName());
		user.setId(resultUser.getId());
		user.setUserToken(vo.getUserToken());
		// 执行修改密码
		int row = userMapper.changePassword(user);
		// 判断是否执行成功
		if (row > 0) {
			// 注销当前登录人的登录信息
			layout(user);
			return ResultJson.buildSuccess("密码修改成功，请重新登陆！");
		}
		return ResultJson.buildError("未找到匹配的用户信息,修改密码操作失败！");
	}
	

	/**
	 * 用户参数校验
	 * 
	 * @param user
	 * @return
	 */
	public ResultJson validateUser(User user) {

		if (user == null) {
			return ResultJson.buildError("参数异常");
		}
		if (StringUtils.isEmpty(user.getUserToken())) {
			return ResultJson.buildError("为空token为空!");
		}

		if (StringUtils.isEmpty(user.getLoginName())) {
			return ResultJson.buildError("登录名为空!");
		}
		if (StringUtils.isEmpty(user.getPhone())) {
			return ResultJson.buildError("手机号码为空!");
		}

		List<Role> roles = user.getRoles();
		if (roles == null || roles.size() < 1) {
			return ResultJson.buildError("用户必须指定角色!");
		} else {
			for (Role role : roles) {
				if (StringUtils.isEmpty(role.getId())) {
					return ResultJson.buildError("被关联的角色缺乏主键!");
				}
			}
		}
		return null;

	}

	/**
	 * 注销登录
	 */
	
	public void layout(User user) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
			LogUtils.debug("用户" + subject.getSession().toString() + "退出登录");
		}
	}

	
	public void saveLoginInfo(HttpServletRequest request, String userId) {
		try {
			String ip = getIpAddr(request);
			
			User user = new User();
			user.setId(userId);
			user.setLoginIp(ip);
			
			userMapper.saveLoginInfo(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/** 
     * 获取当前网络ip 
     * @param request 
     * @return 
     */  
    public String getIpAddr(HttpServletRequest request){  
        String ipAddress = request.getHeader("x-forwarded-for");  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getRemoteAddr();  
                if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                    //根据网卡取本机配置的IP  
                    InetAddress inet=null;  
                    try {  
                        inet = InetAddress.getLocalHost();  
                    } catch (UnknownHostException e) {  
                        e.printStackTrace();  
                    }  
                    ipAddress= inet.getHostAddress();  
                }  
            }  
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
                if(ipAddress.indexOf(",")>0){  
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
                }  
            }  
            return ipAddress;   
    }


}
