package com.born.bc.body.userinfo.service.api;

import com.born.bc.body.commons.entity.PageParamVO;
import com.born.bc.body.commons.exception.BussinessException;
import com.born.bc.body.commons.utils.ResultJson;
import com.born.bc.body.userinfo.entity.Permission;

/**
 * 权限逻辑API
 * @author wangjian
 */
public interface PermissionServiceApi {

    /**
     * 查询权限List
     * @param Permission
     * @return
     */
    public ResultJson list(Permission permission, PageParamVO page);

    /**
     * 添加权限
     * @param Permission
     * @return
     * @throws BussinessException
     */
    public ResultJson add(Permission permission) throws BussinessException;

    /**
     * 修改权限
     * @param Permission
     * @return
     * @throws BussinessException
     */
    public ResultJson update(Permission permission) throws BussinessException;

    /**
     * 删除权限
     * @param id
     * @return
     * @throws BussinessException
     */
    public ResultJson del(String id) throws BussinessException;

    /**
     * 停用启用
     * @param id
     * @param enablestatusEnable
     * @return
     * @throws BussinessException
     */
	public ResultJson updatePermissionEnableStatus(String id, Integer enableStatus) throws BussinessException;

	/**
	 * 根据角色查询角色关联的权限和未关联的权限
	 * @param roleId
	 * @return
	 */
	public ResultJson getPermissionByRole(String roleId);

}
