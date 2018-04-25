package com.born.bc.body.userinfo.dao;

import java.util.HashSet;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.born.bc.body.userinfo.entity.Permission;

/**
 * 权限Mapper
 * @author wangjian
 */
@Mapper
public interface PermissionMapper {
    /**
     * 插入
     * @param record
     * @return
     */
    int insert(Permission record);

    /**
     * 选择性插入
     * @param record
     * @return
     */
    int insertSelective(Permission record);

    /**
     * 根据一组角色ID获取角色对的权限列表
     * @param roleIds
     * @return
     * @throws Exception
     */
    public HashSet<String> getPermissionByRoleCodes(@Param("roleIds") List<String> roleIds);

    /**
     * 查询权限列表
     * @param entity
     * @return
     */
    public List<Permission> selectPermissionList(Permission entity);

    /**
     * 修改权限
     * @param record
     * @return
     */
    public int updateSelective(Permission record);


    /**
     * 查询权限占用记录
     * @param id
     * @return
     */
    Integer checkPermissionIsLinked(@Param("permissionId") String permissionId);

    /**
     * 逻辑删除权限
     * @param id
     * @return
     */
    int updateDelStatus(String id);

    /**
     * 查询角色下面关联的所有权限ID
     * @param roleId
     * @return
     */
	HashSet<String> selectPermissionByRoleId(@Param("roleId")String roleId);

}