package com.born.bc.body.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * @author wangjian
 *
 */
public class LogUtils {

	public static Logger logger = LoggerFactory.getLogger(LogUtils.class);
	
	public static final String ERROR = "ERROR";

	public static final String DEBUG = "DEBUG";
	
	
	private LogUtils(){}

	/**
	 * 输出日志信息
	 * @param msg
	 */
	public static void debug(String msg){
		printLogs(DEBUG, msg, null);
	}
	
	/**
	 * 日志等级DEBUG, 日志输出指定消息msg,以及未限制长度的参数对象objects,
	 * 注意：要去objects中的每个元素都要重写toString方法.
	 * 
	 * @param msg
	 * @param objects
	 */
	public static void debug(String msg, Object ...objects){
		printLogs(DEBUG, msg, null, objects);
	}
	
	/**
	 * 错误信息日志打印
	 * @param msg
	 * @param throwable
	 */
	public static void error(String msg, Throwable throwable){
		printLogs(ERROR, msg, throwable, null);
	}
	
	/**
	 * 错误信息日志打印
	 * @param msg
	 * @param throwable
	 * @param objects
	 */
	public static void error(String msg, Throwable throwable, Object ...objects){
		printLogs(ERROR, msg, throwable, objects);
	}
	
	/**
	 * 日志等级DEBUG, 日志输出指定消息msg,以及未限制长度的参数对象objects,
	 * 注意：要去objects中的每个元素都要重写toString方法.
	 * @param level
	 * @param msg
	 * @param throwable
	 * @param objects
	 */
	private static void printLogs(String level, String msg,Throwable throwable , Object... objects) {
		try {
			StringBuffer buffer = new StringBuffer(2);
			buffer.append(msg);
			if (objects != null && objects.length > 0) {
				buffer.append("---");
				for (Object object : objects) {
					if(object == null) {
						continue;
					}
					// 多个对象使用逗号隔开
					buffer.append(object.toString());
					buffer.append(",");
				}
			}
			
			if(ERROR.equals(level)){
				logger.error(buffer.toString(), throwable);
			}else{
				logger.debug(buffer.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
