package com.born.bc.body.userinfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.born.bc.body.commons.entity.CommonCons;
import com.born.bc.body.commons.entity.PageParamVO;
import com.born.bc.body.commons.exception.BussinessException;
import com.born.bc.body.commons.utils.ResultJson;
import com.born.bc.body.userinfo.entity.Permission;
import com.born.bc.body.userinfo.service.api.PermissionServiceApi;

/**
 * 权限Controller
 * @author wangjian
 */
@RestController
@RequestMapping(value="/permission")
public class PermissionController {
	
	@Autowired
	private PermissionServiceApi api;

    /**
     * 查询权限List
     * @param Permission
     * @param page
     * @return
     */
    @GetMapping(value="/list")
    @ResponseBody
    public ResultJson list(Permission permission, PageParamVO page){
    	return api.list(permission, page);
    }

    /**
     * 添加权限
     * @param Permission
     * @return
     */
    @PostMapping(value="/add")
    @ResponseBody
    public ResultJson add(@RequestBody Permission permission){
    	try {
			return api.add(permission);
		} catch (BussinessException e) {
			return ResultJson.buildError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("操作失败!");
		}
    }

    /**
     * 修改权限
     * @param Permission
     * @return
     */
    @PostMapping(value="/update")
    @ResponseBody
    public ResultJson update(@RequestBody Permission permission){
    	try {
			return api.update(permission);
		} catch (BussinessException e) {
			return ResultJson.buildError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("操作失败!");
		}
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @PostMapping(value="/del")
    @ResponseBody
    public ResultJson del(@RequestParam("id") String id){
    	try {
			return api.del(id);
		} catch (BussinessException e) {
			return ResultJson.buildError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResultJson.buildError("操作失败!");
		}
        
    }
    
    
    /**
     * 启用
     * @param id
     * @return
     */
    @RequestMapping(value="/enable", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson enable(@RequestBody Permission permission){

        try {
            return api.updatePermissionEnableStatus(permission.getId(), CommonCons.EnableStatus_Enable);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJson.buildError("操作失败");
        }
    }

    /**
     * 停用
     * @param id
     * @return
     */
    @RequestMapping(value="/disable", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson disable(@RequestBody Permission permission){

        try {
            return api.updatePermissionEnableStatus(permission.getId(), CommonCons.EnableStatus_Disable);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJson.buildError("操作失败");
        }

    }
    
    /**
     * 请求启用权限列表
     * @return
     */
    @GetMapping(value="/enablePermission")
    public ResultJson enablePermission(){
    	Permission permission = new Permission();
    	permission.setEnableStatus(CommonCons.EnableStatus_Enable);
    	return api.list(permission, new PageParamVO());
    }
    
    
    /**
     * 根据角色获取角色关联的权限以及未关联的权限
     * @return
     */
    @GetMapping(value="/getPermissionByRole")
    public ResultJson getPermissionByRole(@RequestParam("roleId") String roleId){
    	return api.getPermissionByRole(roleId);
    }
    
    

}
