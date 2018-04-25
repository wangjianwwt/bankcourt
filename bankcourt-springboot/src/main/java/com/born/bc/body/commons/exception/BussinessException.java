package com.born.bc.body.commons.exception;

/**
 * 业务异常类
 * @author wangjian
 */
public class BussinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
     * 构造基类
     * @param msg
     */
    public BussinessException(String msg){
        super(msg);
    }


}
