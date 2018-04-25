package com.born.bc.body.userinfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.born.bc.body.commons.entity.CommonCons;
import com.born.bc.body.commons.entity.PageParamVO;
import com.born.bc.body.commons.utils.ResultJson;
import com.born.bc.body.userinfo.entity.Role;
import com.born.bc.body.userinfo.entity.RoleParamVO;
import com.born.bc.body.userinfo.service.api.RoleServiceApi;

/**
 * 角色Controller
 * @author wangjian
 */
@RequestMapping(value="/role")
@RestController
public class RoleController {

    @Autowired
    private RoleServiceApi api;

    /**
     * 查询角色List
     * @param role
     * @return
     */
    @RequestMapping(value="/list")
    @ResponseBody
    public ResultJson list(Role role, PageParamVO page){
        return api.list(role, page);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @RequestMapping(value="/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson add(@RequestBody Role role){
        try {
            return api.add(role);
        } catch (Exception e){
            e.printStackTrace();
            return ResultJson.buildError("添加角色出现系统错误");
        }
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson update(@RequestBody Role role){
        try {
            return api.update(role);
        } catch (Exception e){
            e.printStackTrace();
            return ResultJson.buildError("修改角色出现系统错误");
        }
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @RequestMapping(value="/del", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson del(@RequestBody Role role){
        try {
            return api.del(role.getId());
        } catch (Exception e){
            e.printStackTrace();
            return ResultJson.buildError("删除角色出现系统错误");
        }
    }
    
    /**
     * 启用
     * @param id
     * @return
     */
    @RequestMapping(value="/enable", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson enable(@RequestBody Role role){

        try {
            return api.updateRoleEnableStatus(role.getId(), CommonCons.EnableStatus_Enable);
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
    public ResultJson disable(@RequestBody Role role){

        try {
            return api.updateRoleEnableStatus(role.getId(), CommonCons.EnableStatus_Disable);
        }catch (Exception e){
            e.printStackTrace();
            return ResultJson.buildError("操作失败");
        }

    }

    /**
     * 查询启用角色
     * @return
     */
    @GetMapping(value="/enableRoles")
    public ResultJson enableRoles(){
    	Role role = new Role();
    	role.setEnableStatus(CommonCons.EnableStatus_Enable);
    	return api.list(role, new PageParamVO());
    }
    
    /**
     * 修改角色权限
     * @param role
     * @return
     */
    @PostMapping(value="/updateRoleLinkedPermissions")
    public ResultJson updateRoleLinkedPermissions(@RequestBody RoleParamVO vo){
    	return api.updateRoleLinkedPermissions(vo);
    }
    
    
    @GetMapping(value="/getRoleTemplate")
    public ResultJson getRoleTemplate(){
    	return api.getRoleTemplate();
    }

}
