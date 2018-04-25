package com.born.bc.body.commons.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 编码工具
 * @author wangjian
 */
public class CryptographyUtil {
	
	/**
	 * 进行base64编码
	 * @param str
	 * @return
	 */
	public static String encodeBase64(String str){
		return Base64.encodeToString(str.getBytes());
	}
	
	/**
	 * base64解密
	 * @param str
	 * @return
	 */
	public static String decodeBase64(String str){
		return Base64.decodeToString(str);
	}
	
	/**
	 * MD5加密
	 * @param str 源
	 * @param salt 扰数据
	 * @return
	 */
	public static String md5(String str , String salt){
		return new Md5Hash(str, salt).toString();
	}
	
}
