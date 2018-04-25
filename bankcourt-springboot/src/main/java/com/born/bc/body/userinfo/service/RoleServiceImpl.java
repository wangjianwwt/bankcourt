package com.born.bc.body.userinfo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.util.StringUtils;
import com.born.bc.body.commons.entity.CommonCons;
import com.born.bc.body.commons.entity.CurrentLoginUser;
import com.born.bc.body.commons.entity.PageParamVO;
import com.born.bc.body.commons.exception.BussinessException;
import com.born.bc.body.commons.utils.IdGenerator;
import com.born.bc.body.commons.utils.ResultJson;
import com.born.bc.body.userinfo.dao.RoleMapper;
import com.born.bc.body.userinfo.entity.Permission;
import com.born.bc.body.userinfo.entity.Role;
import com.born.bc.body.userinfo.entity.RoleParamVO;
import com.born.bc.body.userinfo.entity.RolePermissionRelation;
import com.born.bc.body.userinfo.service.api.RoleServiceApi;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * role业务实现类
 *
 * @author wangjian
 */
@Service
public class RoleServiceImpl implements RoleServiceApi {

    @Autowired
    private RoleMapper mapper;

    
    public ResultJson list(Role role, PageParamVO page) {
        try {
        	PageHelper.startPage(page.getPage(), page.getLimit());
        	// 查询角色列表
            List<Role> list = mapper.selectRoleList(role);
            if(CollectionUtils.isEmpty(list)){
            	return ResultJson.buildSuccess("查询成功");
            }
            // 查询角色对应的权限信息
            List<Map<String, String>> permissionList = mapper.selectPermissionListByRole(list);
            if(!CollectionUtils.isEmpty(permissionList)){
            	for(Role rl : list){
            		String roleId = rl.getId();
            		List<Permission> thisPermissionList = new ArrayList<Permission>(7);
            		for(Map<String, String> map : permissionList){
            			if(roleId.equals(map.get("roleId"))){
            				Permission permission = new Permission();
            				permission.setId(map.get("id"));
            				permission.setPermissionCode(map.get("permissionCode"));
            				permission.setPermissionName(map.get("permissionName"));
            				thisPermissionList.add(permission);
            				break;
            			}
            		}
            	}
            }
            PageInfo<Role> pageInfo = new PageInfo<Role>(list);
            return ResultJson.buildSuccess("查询成功!", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultJson.buildError("查询失败!");
        }
    }

    
    @Transactional(rollbackFor = Exception.class)
    public ResultJson add(Role role) throws BussinessException{
    	ResultJson resultJson = validateRole(role);
        if (resultJson != null) {
            return resultJson;
        }

        role.setId(IdGenerator.getUUID());
        // 创建角色
        mapper.insertSelective(role);
        if(!CollectionUtils.isEmpty(role.getPermissions())){
        	// 创建角色权限关系
            batchInsertRoleAndPermission(role);
        }

        return ResultJson.buildSuccess("添加角色成功!");
    }


    
    @Transactional(rollbackFor = Exception.class)
    public ResultJson update(Role role) throws BussinessException{
        // 校验数据
        if(StringUtils.isEmpty(role.getId())){
            return ResultJson.buildParamError("缺乏角色主键!");
        }
        ResultJson resultJson = validateRole(role);
        if (resultJson != null) {
            return resultJson;
        }

        // 修改角色基本信息
        mapper.updateSelective(role);

        return ResultJson.buildSuccess("修改角色关系成功");
    }

    
    @Transactional(rollbackFor = Exception.class)
    public ResultJson del(String id) throws BussinessException{
        if(StringUtils.isEmpty(id)){
            return ResultJson.buildParamError("参数异常!");
        }
        // 校验角色是否被用户关联
        Integer linkedLine = mapper.checkRoleIsLinked(id);
        if(linkedLine != null && linkedLine > 0){
            return ResultJson.buildError("角色已被占用，无法被删除!");
        }

        // 删除角色
        mapper.updateDelStatus(id);
        // 删除角色和权限关联的所有关系
        mapper.updateRoleAndPermissionDelStatus(id);

        return ResultJson.buildSuccess("删除角色关系成功");
    }


    /**
     * 新建角色权限关系
     * @param role
     */
    private void batchInsertRoleAndPermission(Role role){
        // 插入角色所关联的权限到关系表中
        List<Permission> permissions = role.getPermissions();
        // 拿到登录用户
        CurrentLoginUser loginUser = new CurrentLoginUser();
        // 数据转换
        List<RolePermissionRelation> relations = convertFoots(role.getId(), permissions, loginUser);
        // 入库操作
        mapper.batchInsertRoleAndPermission(relations);
    }
    
	
	@Transactional(rollbackFor = Exception.class)
	public ResultJson updateRoleLinkedPermissions(RoleParamVO vo) throws BussinessException {
		if(StringUtils.isEmpty(vo.getId())){
			return ResultJson.buildError("角色主键为空");
		}
		if(vo.getPermissions().length < 1){
			return ResultJson.buildError("未设置权限信息");
		}
		
		//检查角色是否存在
		Role checkRole = mapper.selectById(vo.getId());
		if(checkRole == null){
			return ResultJson.buildError("角色信息不存在，无法进行修改");
		}
	
		// 删除角色和权限关联的所有关系
        mapper.updateRoleAndPermissionDelStatus(vo.getId());
        
        String [] permissionArray = vo.getPermissions();
        // 组装参数
        List<Permission> permissions = new ArrayList<Permission>(permissionArray.length);
        for(String pId : permissionArray){
        	Permission permission = new Permission();
        	permission.setId(pId);
        	permissions.add(permission);
        }
        
        Role role = new Role();
        role.setId(vo.getId());
        role.setPermissions(permissions);
        role.setUserToken(vo.getUserToken());
        
        // 批量新增角色和权限的关系维护数据
        batchInsertRoleAndPermission(role);
		
		return ResultJson.buildSuccess("修改成功！");
	}

    /**
     * 转角色和权限的关系
     *
     * @param roleId
     * @param permissions
     * @param user
     * @return
     */
    private List<RolePermissionRelation> convertFoots(String roleId, List<Permission> permissions, CurrentLoginUser user) {
        Set<String> permissionIdSet = new HashSet<String>();
        for (Permission permission : permissions) {
            permissionIdSet.add(permission.getId());
        }

        List<RolePermissionRelation> relations = new ArrayList<RolePermissionRelation>();
        for (String permissionId : permissionIdSet) {
            RolePermissionRelation relation = new RolePermissionRelation();
            relation.setRoleId(roleId);
            relation.setPermissionId(permissionId);
            relation.setId(IdGenerator.getUUID());
            relation.setFormmakerId(user.getUserId());
            relation.setFormmakerName(user.getLoginName());

            relations.add(relation);
        }

        return relations;
    }
    
	
	@Transactional(rollbackFor = Exception.class)
	public ResultJson updateRoleEnableStatus(String id, Integer enableStatus) throws BussinessException{
		//参数校验
		if(StringUtils.isEmpty(id) || enableStatus == null){
			return ResultJson.buildParamError("参数异常!");
		}
		
		Role role = new Role();
		role.setId(id);
		role.setEnableStatus(enableStatus);
		// 修改角色基本信息
        mapper.updateSelective(role);
		// 返回操作成功
		return ResultJson.buildSuccess("修改成功!");
	}
    

    /**
     * 校验Role
     *
     * @param role
     * @return
     */
    private ResultJson validateRole(Role role) {
        if (role == null) {
            return ResultJson.buildError("参数异常!");
        }
        if (StringUtils.isEmpty(role.getUserToken())) {
            return ResultJson.buildError("用户token为空!");
        }
        if (StringUtils.isEmpty(role.getRoleName())) {
            return ResultJson.buildError("角色名不能为空!");
        }
        return null;
    }

	
	public ResultJson getRoleTemplate() {
		try{
			Role role = new Role();
			role.setEnableStatus(CommonCons.yes);
			List<Role> roleList = mapper.selectRoleList(role);
			return ResultJson.buildSuccess("查询成功", roleList);
		}catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("查询错误");
		}
	}

}
