package com.born.bc.body.commons.utils;

import java.io.Serializable;

import com.born.bc.body.commons.entity.RequestStatusEnum;
import com.github.pagehelper.PageInfo;

/**
 * ResultJson
 * 统一restful接口返回
 * @author wangjian
 *
 */
public class ResultJson implements Serializable {

    private static final long serialVersionUID = -5425361471916352020L;

    /**
     * 状态
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 数据
     */
    private Object data;
    /**
     * 总条数
     */
    private Long count;

    /**
     * 构造函数
     * @param code
     * @param msg
     * @param data
     * @param pageIndex
     * @param pageSize
     * @param count
     */
    public ResultJson(Integer code, String msg, Object data, Long count){
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }


    /**
     * 操作成功返回
     * @param msg
     * @return
     */
    public static ResultJson buildSuccess(String msg){
        return new ResultJson(RequestStatusEnum.SUCCESS.getStatus(),msg,null,null);
    }
    
    /**
     * 操作成功返回
     * @return
     */
    public static ResultJson buildSuccess(){
        return new ResultJson(RequestStatusEnum.SUCCESS.getStatus(),"",null,null);
    }
    
    

    /**
     * 操作成功返回
     * @param msg
     * @param data
     * @return
     */
    public static ResultJson buildSuccess(String msg, Object data){
       return new ResultJson(RequestStatusEnum.SUCCESS.getStatus(),"",data,null);
    }

    /**
     * 操作成功返回
     * @param msg
     * @param pageInfo
     * @return
     */
    public static ResultJson buildSuccess(String msg, PageInfo<?> pageInfo){
        if(pageInfo == null || pageInfo.getList().isEmpty()){
            return new ResultJson(RequestStatusEnum.SUCCESS.getStatus(),"",null,null);
        }

        return new ResultJson(RequestStatusEnum.SUCCESS.getStatus(),"",pageInfo.getList(),pageInfo.getTotal());
    }

    /**
     * 操作失败返回
     * @param msg
     * @return
     */
    public static ResultJson buildError(String msg){
        return new ResultJson(RequestStatusEnum.FAIL_FIELD.getStatus(),msg,null,null);
    }

    /**
     * 操作失败返回
     * @param msg
     * @param data
     * @return
     */
    public static ResultJson buildError(String msg, Object data){
        return new ResultJson(RequestStatusEnum.FAIL_FIELD.getStatus(),msg,data,null);
    }

    /**
     * 操作失败,参数异常返回
     * @param msg
     * @return
     */
    public static ResultJson buildParamError(String msg){
        return new ResultJson(RequestStatusEnum.FAIL_PARAM.getStatus(),msg,null,null);
    }

    /**
     * 身份认证失败，权限不足
     * @return
     */
    public static ResultJson buildUnauthorized(String msg){
    	return new ResultJson(RequestStatusEnum.UAUTHORIZED.getStatus(),msg,null,null);
    }
    
    /**
     * 登录session超时
     * @param msg
     * @return
     */
    public static ResultJson buildSessionOut(String msg){
    	return new ResultJson(RequestStatusEnum.SESSION_OUT.getStatus(),msg,null,null);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }


	@Override
	public String toString() {
		return "{\"code\":" + code + ", \"msg\":" + msg + ", \"data\":" + data + ", \"count\":" + count + "}";
	}
    
    
    
}
