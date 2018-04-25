package com.born.bc.body.userinfo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.born.bc.body.commons.entity.CommonCons;
import com.born.bc.body.commons.entity.CurrentLoginUser;
import com.born.bc.body.commons.entity.PageParamVO;
import com.born.bc.body.commons.exception.BussinessException;
import com.born.bc.body.commons.utils.IdGenerator;
import com.born.bc.body.commons.utils.ResultJson;
import com.born.bc.body.userinfo.dao.PermissionMapper;
import com.born.bc.body.userinfo.entity.Permission;
import com.born.bc.body.userinfo.entity.PermissionLinkDto;
import com.born.bc.body.userinfo.service.api.PermissionServiceApi;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 权限Service
 * 
 * @author wangjian
 */
@Service
public class PermissionServiceImpl implements PermissionServiceApi {

	@Autowired
	private PermissionMapper mapper;

	public ResultJson list(Permission permission, PageParamVO page) {
		try {
			PageHelper.startPage(page.getPage(), page.getLimit());
			List<Permission> list = mapper.selectPermissionList(permission);
			PageInfo<Permission> pageInfo = new PageInfo<Permission>(list);
			return ResultJson.buildSuccess("查询成功!", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildSuccess("查询系统错误!");
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public ResultJson add(Permission permission) throws BussinessException {
		// 校验参数
		ResultJson resultJson = validatePermission(permission);
		if (resultJson != null) {
			return resultJson;
		}
		// 获取当前登录的用户
		CurrentLoginUser user = new CurrentLoginUser();
		permission.setId(IdGenerator.getUUID());
		permission.setFormmakerId(user.getUserId());
		permission.setFormmakerName(user.getLoginName());

		mapper.insertSelective(permission);
		return ResultJson.buildSuccess("新建权限成功!");
	}

	@Transactional(rollbackFor = Exception.class)
	public ResultJson update(Permission permission) throws BussinessException {
		// 校验参数
		if (StringUtils.isEmpty(permission.getId())) {
			return ResultJson.buildParamError("缺乏权限主键!");
		}
		ResultJson resultJson = validatePermission(permission);
		if (resultJson != null) {
			return resultJson;
		}
		mapper.updateSelective(permission);
		return ResultJson.buildSuccess("修改权限成功!");
	}

	@Transactional(rollbackFor = Exception.class)
	public ResultJson del(String id) throws BussinessException {
		if (StringUtils.isEmpty(id)) {
			return ResultJson.buildParamError("参数异常!");
		}
		// 校验权限是否被占用
		Integer linkedLine = mapper.checkPermissionIsLinked(id);
		if (linkedLine != null && linkedLine > 0) {
			return ResultJson.buildError("权限已经被占用,无法被删除!");
		}
		// 执行删除
		mapper.updateDelStatus(id);
		return ResultJson.buildSuccess("删除成功!");
	}

	public ResultJson updatePermissionEnableStatus(String id, Integer enableStatus) throws BussinessException {
		// 参数校验
		if (StringUtils.isEmpty(id) || enableStatus == null) {
			return ResultJson.buildParamError("参数异常!");
		}
		
		Permission permission = new Permission();
		permission.setId(id);
		permission.setEnableStatus(enableStatus);
		mapper.updateSelective(permission);
		return ResultJson.buildSuccess("修改成功!");
	}

	public ResultJson getPermissionByRole(String roleId) {
		try {
			// 查询所有权限
			Permission p = new Permission();
			p.setEnableStatus(CommonCons.yes);
			List<Permission> allPermissionList = mapper.selectPermissionList(p); 
			
			if(CollectionUtils.isEmpty(allPermissionList)){
				return ResultJson.buildSuccess("查询无结果");
			}
			//初始化 返回集合
			List<PermissionLinkDto> resultList = new ArrayList<PermissionLinkDto>(allPermissionList.size());
			for(Permission thisP : allPermissionList){
				PermissionLinkDto dto = new PermissionLinkDto();
				dto.setIsLinked(CommonCons.no);
				dto.setPermissionId(thisP.getId());
				dto.setPermissionName(thisP.getPermissionName());
				resultList.add(dto);
			}
			
			//根据角色Id获取该角色Id下关联的所有权限
			HashSet<String> permissionsSet = mapper.selectPermissionByRoleId(roleId);
			if(CollectionUtils.isEmpty(permissionsSet)){
				return ResultJson.buildSuccess("查询成功",resultList);
			}
			// 匹配设置关联
			for(PermissionLinkDto dto : resultList){
				for(String permissionId : permissionsSet){
					//匹配权限Id,如果匹配，则表示已经关联
					if(dto.getPermissionId().equals(permissionId)){
						dto.setIsLinked(CommonCons.yes);
					}
				}
			}
			
			return ResultJson.buildSuccess("查询成功",resultList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("查询错误！");
		}
	}

	
	/**
	 * 验证权限参数
	 * 
	 * @param permission
	 * @return
	 */
	private ResultJson validatePermission(Permission permission) {
		if (permission == null) {
			return ResultJson.buildError("参数异常!");
		}
		if (StringUtils.isEmpty(permission.getUserToken())) {
			return ResultJson.buildError("用户token为空!");
		}
		if (StringUtils.isEmpty(permission.getPermissionName())) {
			return ResultJson.buildError("权限名为空!");
		}
		return null;
	}


}
