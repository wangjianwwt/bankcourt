package com.born.bc.body.userinfo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.born.bc.body.userinfo.entity.User;
import com.born.bc.body.userinfo.entity.UserRoleRelation;

/**
 * 用户Mapper
 * @author wangjian
 */
@Mapper
public interface UserMapper {

    /**
     * 选择性插入
     * @param record
     * @return
     */
    int insertSelective(User record);

    /**
     * 选择性修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);



    /**
     * 根据loginName匹配用户登录名或者手机号码
     * @param loginName
     * @return
     * @
     */
    public User getUserByLoginName(@Param("loginName") String loginName);

    /**
     * 验证用的登录名和手机号码是否同时存在
     * @param id
     * @param loginName
     * @param phone
     * @return
     * @
     */
    public ArrayList<User> validateUserByLoginName(@Param("id") String id, @Param("loginName") String loginName, @Param("phone") String phone);

    /**
     * 根据用户ID将用户逻辑删除
     * @param id
     * @return
     * @
     */
    public int updateUserDelStatus(@Param("id") String id);

    /**
     * 查询用户列表
     * @param user
     * @return
     * @
     */
    public ArrayList<User> findUserList(@Param("entity") User user);

    /**
     * 增加用户角色关系
     * @param list
     * @return
     */
    public int batchInsertUserRoleRelation(@Param("list") List<UserRoleRelation> list);

    /**
     * 逻辑删除用户角色关系
     * @param userId
     */
    int updateUserAndRoleDelStatus(@Param("userId") String userId);

    /**
     * 根据ID查询用户详情
     * @param id
     * @return
     */
    User selectById(@Param("id") String id);

    /**
     * 重置密码
     * @param user
     * @return
     */
	int changePassword(User user);

	/**
	 * 根据用户ID查询角色
	 * @param list
	 * @return
	 */
	ArrayList<Map<String, String>> findRoleListByUserIds(@Param("list") List<User> list);
	
	/**
	 * 验证密码是否正确
	 * @param id
	 * @param oldMd5Pwd
	 * @return
	 */
	Integer validateOldMd5Pwd(@Param("id")String id, @Param("password")String password);

	int saveLoginInfo(User user);



}