package com.born.bc.body.commons.utils;

import java.util.UUID;

/**
 * ID生成器
 * @author wangjian
 */
public class IdGenerator {

    /**
     * 私有构造器
     */
    private IdGenerator(){}

    /**
     * 得到32bit UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

}
