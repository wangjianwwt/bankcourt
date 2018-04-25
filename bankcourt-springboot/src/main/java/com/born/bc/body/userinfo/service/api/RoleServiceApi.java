package com.born.bc.body.userinfo.service.api;

import com.born.bc.body.commons.entity.PageParamVO;
import com.born.bc.body.commons.exception.BussinessException;
import com.born.bc.body.commons.utils.ResultJson;
import com.born.bc.body.userinfo.entity.Role;
import com.born.bc.body.userinfo.entity.RoleParamVO;

/**
 * 角色逻辑API
 * @author wangjian
 */
public interface RoleServiceApi {

    /**
     * 获取角色列表
     * @param role
     * @param page
     * @return
     */
    public ResultJson list(Role role, PageParamVO page);

    /**
     * 添加角色
     * @param role
     * @return
     */
    public ResultJson add(Role role) throws BussinessException;

    /**
     * 修改角色
     * @param role
     * @return
     */
    public ResultJson update(Role role) throws BussinessException;

    /**
     * 删除角色
     * @param id
     * @return
     */
    public ResultJson del(String id) throws BussinessException;

    /**
     * 停用启用接口
     * @param id
     * @param enablestatusDisable
     * @return
     */
	public ResultJson updateRoleEnableStatus(String id, Integer enableStatus) throws BussinessException;

	/**
	 * 修改
	 * @return
	 * @throws BussinessException
	 */
	public ResultJson updateRoleLinkedPermissions(RoleParamVO vo) throws BussinessException;

	/**
	 * 得到角色参照
	 * @return
	 */
	public ResultJson getRoleTemplate();


}
