package com.born.bc.body.userinfo.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.born.bc.body.commons.utils.ResultJson;

@RestController
@RequestMapping(value="/msg")
public class MsgController {

    /**
     * 404 配置请求
     * @return
     */
    @RequestMapping(value="/outOfContact")
    public ResultJson requestOutOfContact(){
        return ResultJson.buildError("您的请求失联了......</ br> 错误码：404");
    }

    /**
     * 500 配置请求
     * @return
     */
    @RequestMapping(value="/error")
    public ResultJson requestError(){
        return ResultJson.buildError("当前网络环境异常,请稍后再试......</ br> 错误码：500");
    }

    /**
     * 没有权限
     * @return
     */
    @RequestMapping(value="/unauthorized")
    public ResultJson unauthorized() { return ResultJson.buildUnauthorized("您沒有权限请求该页面......</ br> 错误码：402"); }
    
    /**
     * session过期
     * @return
     */
    @RequestMapping(value="/sessionout")
    public ResultJson sessionout(HttpServletResponse response) { 
    	
    	// 清除浏览器cookie
    	Cookie tokenCookie = new Cookie("token",null);
    	response.addCookie(tokenCookie);
    	Cookie jsessionCookie = new Cookie("JSESSIONID",null);
    	response.addCookie(jsessionCookie);
    	Cookie uIdCookie = new Cookie("uid",null);
    	response.addCookie(uIdCookie);
    	Cookie uNameCookie = new Cookie("uname",null);
    	response.addCookie(uNameCookie);
    	Cookie roleCookie = new Cookie("role",null);
    	response.addCookie(roleCookie);
    	
    	return ResultJson.buildSessionOut("当前会话已过期，请重新登录......</ br> 错误码：403"); 
    }
}